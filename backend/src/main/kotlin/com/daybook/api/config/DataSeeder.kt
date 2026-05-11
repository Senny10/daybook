package com.daybook.api.config

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.enum.EntryType
import com.daybook.api.domain.model.Account
import com.daybook.api.domain.model.Entry
import com.daybook.api.domain.model.Transaction
import com.daybook.api.domain.model.User
import com.daybook.api.domain.model.UserRole
import com.daybook.api.repository.AccountRepository
import com.daybook.api.repository.EntryRepository
import com.daybook.api.repository.TransactionRepository
import com.daybook.api.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.time.LocalDate

@Component
@Profile("seed")
class DataSeeder(
    private val accountRepository: AccountRepository,
    private val transactionRepository: TransactionRepository,
    private val entryRepository: EntryRepository,
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String?) {
        if (accountRepository.count() > 0) {
            println("Seed data already exists — skipping.")
            return
        }

        println("🌞 Seeding Daybook demo data...")

        // ── Users ──────────────────────────────────────────────
        val admin = userRepository.save(User(
            username = "admin",
            password = passwordEncoder.encode("admin123!"),
            role = UserRole.ADMIN
        ))
        userRepository.save(User(
            username = "bookkeeper",
            password = passwordEncoder.encode("book123!"),
            role = UserRole.USER
        ))

        // ── Chart of Accounts ──────────────────────────────────
        val cash = accountRepository.save(Account(
            name = "Cash",
            type = AccountType.ASSET,
            description = "Main business cash account"
        ))
        val accountsReceivable = accountRepository.save(Account(
            name = "Accounts Receivable",
            type = AccountType.ASSET,
            description = "Money owed by customers"
        ))
        val officeEquipment = accountRepository.save(Account(
            name = "Office Equipment",
            type = AccountType.ASSET,
            description = "Computers, furniture, and equipment"
        ))
        val accountsPayable = accountRepository.save(Account(
            name = "Accounts Payable",
            type = AccountType.LIABILITY,
            description = "Money owed to suppliers"
        ))
        val ownersCapital = accountRepository.save(Account(
            name = "Owner's Capital",
            type = AccountType.EQUITY,
            description = "Owner's investment in the business"
        ))
        val serviceRevenue = accountRepository.save(Account(
            name = "Service Revenue",
            type = AccountType.REVENUE,
            description = "Income from consulting services"
        ))
        val rentExpense = accountRepository.save(Account(
            name = "Rent Expense",
            type = AccountType.EXPENSE,
            description = "Monthly office rent"
        ))
        val salariesExpense = accountRepository.save(Account(
            name = "Salaries Expense",
            type = AccountType.EXPENSE,
            description = "Staff salaries"
        ))

        // ── Transactions ───────────────────────────────────────

        // T1: Owner invests £10,000 to start the business
        postTransaction(
            date = LocalDate.of(2026, 1, 1),
            description = "Owner's initial investment",
            reference = "TXN-001",
            entries = listOf(
                Triple(cash, BigDecimal("10000.00"), EntryType.DEBIT),
                Triple(ownersCapital, BigDecimal("10000.00"), EntryType.CREDIT)
            )
        )

        // T2: Purchased office equipment for £2,500 cash
        postTransaction(
            date = LocalDate.of(2026, 1, 15),
            description = "Purchased office equipment",
            reference = "TXN-002",
            entries = listOf(
                Triple(officeEquipment, BigDecimal("2500.00"), EntryType.DEBIT),
                Triple(cash, BigDecimal("2500.00"), EntryType.CREDIT)
            )
        )

        // T3: Provided consulting services, invoiced £3,000
        postTransaction(
            date = LocalDate.of(2026, 2, 1),
            description = "Consulting services — Invoice INV-001",
            reference = "TXN-003",
            entries = listOf(
                Triple(accountsReceivable, BigDecimal("3000.00"), EntryType.DEBIT),
                Triple(serviceRevenue, BigDecimal("3000.00"), EntryType.CREDIT)
            )
        )

        // T4: Customer paid invoice INV-001
        postTransaction(
            date = LocalDate.of(2026, 2, 10),
            description = "Customer payment — INV-001 settled",
            reference = "TXN-004",
            entries = listOf(
                Triple(cash, BigDecimal("3000.00"), EntryType.DEBIT),
                Triple(accountsReceivable, BigDecimal("3000.00"), EntryType.CREDIT)
            )
        )

        // T5: Paid monthly expenses (rent + salaries)
        postTransaction(
            date = LocalDate.of(2026, 2, 28),
            description = "February operating expenses",
            reference = "TXN-005",
            entries = listOf(
                Triple(rentExpense, BigDecimal("1200.00"), EntryType.DEBIT),
                Triple(salariesExpense, BigDecimal("4000.00"), EntryType.DEBIT),
                Triple(cash, BigDecimal("5200.00"), EntryType.CREDIT)
            )
        )

        println("✅ Seed data loaded successfully!")
        println("   Accounts: ${accountRepository.count()}")
        println("   Transactions: ${transactionRepository.count()}")
        println("   Users: admin / admin123! and bookkeeper / book123!")
    }

    private fun postTransaction(
        date: LocalDate,
        description: String,
        reference: String,
        entries: List<Triple<Account, BigDecimal, EntryType>>
    ) {
        val transaction = transactionRepository.save(
            Transaction(
                date = date,
                description = description,
                reference = reference
            )
        )
        entries.forEach { (account, amount, type) ->
            entryRepository.save(
                Entry(
                    transaction = transaction,
                    account = account,
                    amount = amount,
                    type = type
                )
            )
        }
    }
}