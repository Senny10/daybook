package com.daybook.api.controller

import com.daybook.api.config.TestSecurityConfig
import com.daybook.api.domain.enum.AccountType
import com.daybook.api.domain.model.Account
import com.daybook.api.service.AccountService
import com.daybook.api.service.LedgerService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(AccountController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig::class)
class AccountControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var accountService: AccountService

    @MockitoBean
    lateinit var ledgerService: LedgerService

    @MockitoBean
    lateinit var jwtService: com.daybook.api.security.JwtService

    @MockitoBean
    lateinit var daybookUserDetailsService: com.daybook.api.security.DaybookUserDetailsService

    @Test
    fun `GET accounts should return 200 with list of accounts`() {
        // Arrange
        val accounts = listOf(
            Account(id = 1L, name = "Cash",
                type = AccountType.ASSET, description = ""),
            Account(id = 2L, name = "Revenue",
                type = AccountType.REVENUE, description = "")
        )
        whenever(accountService.getAllAccounts()).thenReturn(accounts)

        // Act & Assert
        mockMvc.perform(get("/api/accounts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Cash"))
            .andExpect(jsonPath("$[0].type").value("ASSET"))
            .andExpect(jsonPath("$[1].name").value("Revenue"))
    }

    @Test
    fun `POST accounts should return 201 with created account`() {
        // Arrange
        val savedAccount = Account(
            id = 1L,
            name = "Cash",
            type = AccountType.ASSET,
            description = "Main cash account"
        )
        whenever(accountService.createAccount(any(), any(), any()))
            .thenReturn(savedAccount)

        val requestBody = mapOf(
            "name" to "Cash",
            "type" to "ASSET",
            "description" to "Main cash account"
        )

        // Act & Assert
        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Cash"))
            .andExpect(jsonPath("$.type").value("ASSET"))
    }

    @Test
    fun `POST accounts should return 400 when name is blank`() {
        val requestBody = mapOf(
            "name" to "",
            "type" to "ASSET"
        )

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation Failed"))
    }

    @Test
    fun `POST accounts should return 400 when name already exists`() {
        whenever(accountService.createAccount(any(), any(), any()))
            .thenThrow(IllegalArgumentException("Account with name 'Cash' already exists"))

        val requestBody = mapOf(
            "name" to "Cash",
            "type" to "ASSET"
        )

        mockMvc.perform(
            post("/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Account with name 'Cash' already exists"))
    }

    @Test
    fun `GET accounts by id should return 200 with account`() {
        val account = Account(
            id = 1L,
            name = "Cash",
            type = AccountType.ASSET,
            description = ""
        )
        whenever(accountService.getAccountById(1L)).thenReturn(account)

        mockMvc.perform(get("/api/accounts/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Cash"))
    }

    @Test
    fun `GET accounts by id should return 400 when account not found`() {
        whenever(accountService.getAccountById(99L))
            .thenThrow(IllegalArgumentException("Account not found with id: 99"))

        mockMvc.perform(get("/api/accounts/99"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Account not found with id: 99"))
    }

    @Test
    fun `GET account balance should return 200 with balance`() {
        val account = Account(
            id = 1L,
            name = "Cash",
            type = AccountType.ASSET,
            description = ""
        )
        whenever(accountService.getAccountById(1L)).thenReturn(account)
        whenever(ledgerService.getAccountBalance(1L))
            .thenReturn(BigDecimal("500.00"))

        mockMvc.perform(get("/api/accounts/1/balance"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accountId").value(1))
            .andExpect(jsonPath("$.accountName").value("Cash"))
            .andExpect(jsonPath("$.balance").value(500.00))
    }
}