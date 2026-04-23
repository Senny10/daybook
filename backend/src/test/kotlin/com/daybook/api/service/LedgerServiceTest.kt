package com.daybook.api.service

import com.daybook.api.domain.enum.EntryType
import com.daybook.api.repository.AccountRepository
import com.daybook.api.repository.EntryRepository
import com.daybook.api.repository.TransactionRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class LedgerServiceTest {
    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var entryRepository: EntryRepository

    @Mock
    private lateinit var accountRepository: AccountRepository

    private lateinit var ledgerService: LedgerService

    @BeforeEach
    fun setUp() {
        ledgerService =
            LedgerService(
                transactionRepository,
                entryRepository,
                accountRepository,
            )
    }

    @Test
    fun `postTransaction should throw when no DEBIT entries present`() {
        val entries =
            listOf(
                LedgerService.EntryRequest(
                    accountId = 1,
                    amount = BigDecimal("500.00"),
                    type = EntryType.CREDIT,
                ),
                LedgerService.EntryRequest(
                    accountId = 2,
                    amount = BigDecimal("500.00"),
                    type = EntryType.CREDIT,
                ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                ledgerService.postTransaction(
                    date = LocalDate.now(),
                    description = "Test transaction",
                    reference = "TXN-TEST-001",
                    entryRequests = entries,
                )
            }

        assertTrue(
            exception.message!!.contains("DEBIT"),
            "Error message should mention DEBIT",
        )
    }

    @Test
    fun `postTransaction should throw when no CREDIT entries present`() {
        val entries =
            listOf(
                LedgerService.EntryRequest(
                    accountId = 1,
                    amount = BigDecimal("500.00"),
                    type = EntryType.DEBIT,
                ),
                LedgerService.EntryRequest(
                    accountId = 2,
                    amount = BigDecimal("500.00"),
                    type = EntryType.DEBIT,
                ),
            )

        val exception =
            assertThrows<IllegalArgumentException> {
                ledgerService.postTransaction(
                    date = LocalDate.now(),
                    description = "Test transaction",
                    reference = "TXN-TEST-002",
                    entryRequests = entries,
                )
            }

        assertTrue(
            exception.message!!.contains("CREDIT"),
            "Error message should mention CREDIT",
        )
    }

    @Test
    fun `postTransaction should throw when entries are empty`() {
        val entries = emptyList<LedgerService.EntryRequest>()

        val exception =
            assertThrows<IllegalArgumentException> {
                ledgerService.postTransaction(
                    date = LocalDate.now(),
                    description = "Test transaction",
                    reference = "TXN-TEST-003",
                    entryRequests = entries,
                )
            }

        assertTrue(
            exception.message!!.contains("two entries"),
            "Error message should mention minimum entries requirement",
        )
    }
}
