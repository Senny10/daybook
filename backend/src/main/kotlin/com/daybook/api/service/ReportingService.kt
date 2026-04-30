package com.daybook.api.service

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.enum.EntryType
import com.daybook.api.dto.response.BalanceSheetResponse
import com.daybook.api.dto.response.ProfitAndLossResponse
import com.daybook.api.dto.response.TrialBalanceResponse
import com.daybook.api.repository.AccountRepository
import com.daybook.api.repository.EntryRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ReportingService(
    private val accountRepository: AccountRepository,
    private val entryRepository: EntryRepository
) {

    fun getTrialBalance(): TrialBalanceResponse {
        val accounts = accountRepository.findAll()

        val entries = accounts.map { account ->
            val debits = entryRepository.sumAmountByAccountIdAndType(
                account.id!!, EntryType.DEBIT
            )
            val credits = entryRepository.sumAmountByAccountIdAndType(
                account.id!!, EntryType.CREDIT
            )
            TrialBalanceResponse.TrialBalanceEntry(
                accountId = account.id!!,
                accountName = account.name,
                accountType = account.type.name,
                debitBalance = debits,
                creditBalance = credits
            )
        }

        val totalDebits = entries.sumOf { it.debitBalance }
        val totalCredits = entries.sumOf { it.creditBalance }

        return TrialBalanceResponse(
            entries = entries,
            totalDebits = totalDebits,
            totalCredits = totalCredits,
            isBalanced = totalDebits == totalCredits
        )
    }

    fun getProfitAndLoss(): ProfitAndLossResponse {
        val accounts = accountRepository.findAll()

        val totalRevenue = accounts
            .filter { it.type == AccountType.REVENUE }
            .sumOf { account ->
                entryRepository.sumAmountByAccountIdAndType(
                    account.id!!, EntryType.CREDIT
                ) - entryRepository.sumAmountByAccountIdAndType(
                    account.id!!, EntryType.DEBIT
                )
            }

        val totalExpenses = accounts
            .filter { it.type == AccountType.EXPENSE }
            .sumOf { account ->
                entryRepository.sumAmountByAccountIdAndType(
                    account.id!!, EntryType.DEBIT
                ) - entryRepository.sumAmountByAccountIdAndType(
                    account.id!!, EntryType.CREDIT
                )
            }

        val netIncome = totalRevenue - totalExpenses

        return ProfitAndLossResponse(
            totalRevenue = totalRevenue,
            totalExpenses = totalExpenses,
            netIncome = netIncome,
            isProfit = netIncome >= BigDecimal.ZERO
        )
    }

    fun getBalanceSheet(): BalanceSheetResponse {
        val accounts = accountRepository.findAll()

        fun accountBalance(
            accountId: Long,
            normalBalanceType: EntryType
        ): BigDecimal {
            val debits = entryRepository.sumAmountByAccountIdAndType(
                accountId, EntryType.DEBIT
            )
            val credits = entryRepository.sumAmountByAccountIdAndType(
                accountId, EntryType.CREDIT
            )
            return if (normalBalanceType == EntryType.DEBIT)
                debits - credits
            else
                credits - debits
        }

        val totalAssets = accounts
            .filter { it.type == AccountType.ASSET }
            .sumOf { accountBalance(it.id!!, EntryType.DEBIT) }

        val totalLiabilities = accounts
            .filter { it.type == AccountType.LIABILITY }
            .sumOf { accountBalance(it.id!!, EntryType.CREDIT) }

        val totalEquity = accounts
            .filter { it.type == AccountType.EQUITY }
            .sumOf { accountBalance(it.id!!, EntryType.CREDIT) }

        val totalLiabilitiesAndEquity = totalLiabilities + totalEquity

        return BalanceSheetResponse(
            totalAssets = totalAssets,
            totalLiabilities = totalLiabilities,
            totalEquity = totalEquity,
            isBalanced = totalAssets == totalLiabilitiesAndEquity
        )
    }
}