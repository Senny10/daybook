package com.daybook.api.service

import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.model.Account
import com.daybook.api.repository.AccountRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AccountServiceTest {

    @Mock
    private lateinit var accountRepository: AccountRepository

    private lateinit var accountService: AccountService

    @BeforeEach
    fun setUp() {
        accountService = AccountService(accountRepository)
    }

    @Test
    fun `createAccount should save and return account when name is unique`() {
        // Arrange
        val expectedAccount = Account(
            id = 1L,
            name = "Cash",
            type = AccountType.ASSET,
            description = "Main cash account"
        )
        whenever(accountRepository.existsByName("Cash"))
            .thenReturn(false)

        // Use doReturn to bypass Mockito-Kotlin null safety check
        org.mockito.Mockito.doReturn(expectedAccount)
            .`when`(accountRepository).save(any())

        // Act
        val result = accountService.createAccount(
            name = "Cash",
            type = AccountType.ASSET,
            description = "Main cash account"
        )

        // Assert
        assertEquals("Cash", result.name)
        assertEquals(AccountType.ASSET, result.type)
        verify(accountRepository).save(any())
    }

    @Test
    fun `createAccount should throw when account name already exists`() {
        // Arrange
        whenever(accountRepository.existsByName("Cash"))
            .thenReturn(true)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            accountService.createAccount(
                name = "Cash",
                type = AccountType.ASSET
            )
        }

        assertTrue(exception.message!!.contains("Cash"))
        verify(accountRepository, never()).save(any())
    }

    @Test
    fun `getAccountById should return account when it exists`() {
        // Arrange
        val account = Account(
            id = 1L,
            name = "Cash",
            type = AccountType.ASSET,
            description = ""
        )
        whenever(accountRepository.findById(1L))
            .thenReturn(Optional.of(account))

        // Act
        val result = accountService.getAccountById(1L)

        // Assert
        assertEquals(1L, result.id)
        assertEquals("Cash", result.name)
    }

    @Test
    fun `getAccountById should throw when account does not exist`() {
        // Arrange
        whenever(accountRepository.findById(99L))
            .thenReturn(Optional.empty())

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            accountService.getAccountById(99L)
        }

        assertTrue(exception.message!!.contains("99"))
    }

    @Test
    fun `getAllAccounts should return all accounts`() {
        // Arrange
        val accounts = listOf(
            Account(id = 1L, name = "Cash",
                type = AccountType.ASSET, description = ""),
            Account(id = 2L, name = "Revenue",
                type = AccountType.REVENUE, description = "")
        )
        whenever(accountRepository.findAll())
            .thenReturn(accounts)

        // Act
        val result = accountService.getAllAccounts()

        // Assert
        assertEquals(2, result.size)
        verify(accountRepository).findAll()
    }

    @Test
    fun `getAccountsByType should return only accounts of given type`() {
        // Arrange
        val assetAccounts = listOf(
            Account(id = 1L, name = "Cash",
                type = AccountType.ASSET, description = ""),
            Account(id = 2L, name = "Equipment",
                type = AccountType.ASSET, description = "")
        )
        whenever(accountRepository.findByType(AccountType.ASSET))
            .thenReturn(assetAccounts)

        // Act
        val result = accountService.getAccountsByType(AccountType.ASSET)

        // Assert
        assertEquals(2, result.size)
        assertTrue(result.all { it.type == AccountType.ASSET })
    }
}