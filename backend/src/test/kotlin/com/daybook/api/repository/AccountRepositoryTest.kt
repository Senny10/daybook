package com.daybook.api.repository

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.enum.EntryType
import com.daybook.api.domain.model.Account
import com.daybook.api.domain.model.Entry
import com.daybook.api.domain.model.Transaction
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import java.math.BigDecimal
import java.time.LocalDate

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    lateinit var entityManager: TestEntityManager

    @Autowired
    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var entryRepository: EntryRepository

    @Test
    fun `should save and find account by name`() {
        // Arrange
        val account = Account(
            name = "Cash",
            type = AccountType.ASSET,
            description = "Main cash account"
        )
        entityManager.persist(account)
        entityManager.flush()

        // Act
        val found = accountRepository.findByName("Cash")

        // Assert
        assertNotNull(found)
        assertEquals("Cash", found!!.name)
        assertEquals(AccountType.ASSET, found.type)
    }

    @Test
    fun `should return null when account name does not exist`() {
        val found = accountRepository.findByName("NonExistent")
        assertNull(found)
    }

    @Test
    fun `existsByName should return true when account exists`() {
        val account = Account(
            name = "Revenue",
            type = AccountType.REVENUE,
            description = ""
        )
        entityManager.persist(account)
        entityManager.flush()

        assertTrue(accountRepository.existsByName("Revenue"))
        assertFalse(accountRepository.existsByName("NonExistent"))
    }

    @Test
    fun `should find accounts by type`() {
        entityManager.persist(Account(
            name = "Cash",
            type = AccountType.ASSET,
            description = ""
        ))
        entityManager.persist(Account(
            name = "Equipment",
            type = AccountType.ASSET,
            description = ""
        ))
        entityManager.persist(Account(
            name = "Revenue",
            type = AccountType.REVENUE,
            description = ""
        ))
        entityManager.flush()

        val assets = accountRepository.findByType(AccountType.ASSET)
        assertEquals(2, assets.size)
        assertTrue(assets.all { it.type == AccountType.ASSET })
    }

    @Test
    fun `sumAmountByAccountIdAndType should return correct balance`() {
        // Arrange — create accounts and a transaction
        val cashAccount = Account(
            name = "Cash",
            type = AccountType.ASSET,
            description = ""
        )
        val revenueAccount = Account(
            name = "Revenue",
            type = AccountType.REVENUE,
            description = ""
        )
        entityManager.persist(cashAccount)
        entityManager.persist(revenueAccount)

        val transaction = Transaction(
            date = LocalDate.now(),
            description = "Test transaction",
            reference = "TXN-001"
        )
        entityManager.persist(transaction)

        entityManager.persist(Entry(
            transaction = transaction,
            account = cashAccount,
            amount = BigDecimal("500.00"),
            type = EntryType.DEBIT
        ))
        entityManager.persist(Entry(
            transaction = transaction,
            account = revenueAccount,
            amount = BigDecimal("500.00"),
            type = EntryType.CREDIT
        ))
        entityManager.flush()

        // Act
        val debitSum = entryRepository.sumAmountByAccountIdAndType(
            cashAccount.id!!,
            EntryType.DEBIT
        )
        val creditSum = entryRepository.sumAmountByAccountIdAndType(
            revenueAccount.id!!,
            EntryType.CREDIT
        )

        // Assert
        assertEquals(BigDecimal("500.0000"), debitSum)
        assertEquals(BigDecimal("500.0000"), creditSum)
    }
}