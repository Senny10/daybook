package com.daybook.api.service

import com.daybook.api.domain.enum.EntryType
import com.daybook.api.domain.model.Entry
import com.daybook.api.domain.model.Transaction
import com.daybook.api.repository.AccountRepository
import com.daybook.api.repository.EntryRepository
import com.daybook.api.repository.TransactionRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class LedgerService(
    private val transactionRepository: TransactionRepository,
    private val entryRepository: EntryRepository,
    private val accountRepository: AccountRepository,
) {
    data class EntryRequest(
        val accountId: Long,
        val amount: BigDecimal,
        val type: EntryType,
    )

    @Transactional
    fun postTransaction(
        date: LocalDate,
        description: String,
        reference: String,
        entryRequests: List<EntryRequest>,
    ): Transaction {
        // Rule 1: Must have at least two entries
        if (entryRequests.size < 2) {
            throw IllegalArgumentException(
                "A transaction must have at least two entries",
            )
        }

        // Rule 2: Must have at least one DEBIT and one CREDIT
        validateEntryTypes(entryRequests)

        // Rule 3: Debits must equal credits
        validateDoubleEntry(entryRequests)

        // Rule 4: Reference must be unique
        if (transactionRepository.existsByReference(reference)) {
            throw IllegalArgumentException(
                "Transaction with reference '$reference' already exists",
            )
        }

        // Rule 5: All amounts must be positive
        if (entryRequests.any { it.amount <= BigDecimal.ZERO }) {
            throw IllegalArgumentException(
                "All entry amounts must be positive",
            )
        }

        // Save transaction
        val transaction =
            transactionRepository.save(
                Transaction(
                    date = date,
                    description = description,
                    reference = reference,
                ),
            )

        // Save entries
        entryRequests.forEach { request ->
            val account =
                accountRepository
                    .findById(request.accountId)
                    .orElseThrow {
                        IllegalArgumentException(
                            "Account not found with id: ${request.accountId}",
                        )
                    }
            entryRepository.save(
                Entry(
                    transaction = transaction,
                    account = account,
                    amount = request.amount,
                    type = request.type,
                ),
            )
        }

        return transaction
    }

    private fun validateEntryTypes(entries: List<EntryRequest>) {
        val hasDebit = entries.any { it.type == EntryType.DEBIT }
        val hasCredit = entries.any { it.type == EntryType.CREDIT }

        if (!hasDebit) {
            throw IllegalArgumentException(
                "Transaction must have at least one DEBIT entry",
            )
        }
        if (!hasCredit) {
            throw IllegalArgumentException(
                "Transaction must have at least one CREDIT entry",
            )
        }
    }

    private fun validateDoubleEntry(entries: List<EntryRequest>) {
        val totalDebits =
            entries
                .filter { it.type == EntryType.DEBIT }
                .sumOf { it.amount }

        val totalCredits =
            entries
                .filter { it.type == EntryType.CREDIT }
                .sumOf { it.amount }

        if (totalDebits != totalCredits) {
            throw IllegalArgumentException(
                "Debits ($totalDebits) must equal credits ($totalCredits)",
            )
        }
    }

    fun getAccountBalance(accountId: Long): BigDecimal {
        val account =
            accountRepository
                .findById(accountId)
                .orElseThrow {
                    IllegalArgumentException(
                        "Account not found with id: $accountId",
                    )
                }

        val debits =
            entryRepository.sumAmountByAccountIdAndType(
                accountId,
                EntryType.DEBIT,
            )
        val credits =
            entryRepository.sumAmountByAccountIdAndType(
                accountId,
                EntryType.CREDIT,
            )

        return when (account.type) {
            com.daybook.api.domain.enum.AccountType.ASSET,
            com.daybook.api.domain.enum.AccountType.EXPENSE,
            -> debits - credits

            com.daybook.api.domain.enum.AccountType.LIABILITY,
            com.daybook.api.domain.enum.AccountType.EQUITY,
            com.daybook.api.domain.enum.AccountType.REVENUE,
            -> credits - debits
        }
    }

    fun getAllTransactions(): List<Transaction> =
        transactionRepository.findAllWithEntries()

    fun getTransactionById(id: Long): Transaction =
        transactionRepository.findByIdWithEntries(id)
            ?: throw IllegalArgumentException(
                "Transaction not found with id: $id"
            )
}
