package com.daybook.api.controller

import com.daybook.api.config.TestSecurityConfig
import com.daybook.api.domain.model.Transaction
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
import java.time.LocalDate

@WebMvcTest(TransactionController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig::class)
class TransactionControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var ledgerService: LedgerService

    @MockitoBean
    lateinit var jwtService: com.daybook.api.security.JwtService

    @MockitoBean
    lateinit var daybookUserDetailsService: com.daybook.api.security.DaybookUserDetailsService

    @Test
    fun `POST transactions should return 201 with created transaction`() {
        // Arrange
        val savedTransaction = Transaction(
            id = 1L,
            date = LocalDate.of(2026, 4, 30),
            description = "Received payment for services",
            reference = "TXN-001"
        )
        whenever(ledgerService.postTransaction(any(), any(), any(), any()))
            .thenReturn(savedTransaction)

        val requestBody = mapOf(
            "date" to "2026-04-30",
            "description" to "Received payment for services",
            "reference" to "TXN-001",
            "entries" to listOf(
                mapOf("accountId" to 1, "amount" to 500.00, "type" to "DEBIT"),
                mapOf("accountId" to 2, "amount" to 500.00, "type" to "CREDIT")
            )
        )

        // Act & Assert
        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.reference").value("TXN-001"))
            .andExpect(jsonPath("$.description")
                .value("Received payment for services"))
    }

    @Test
    fun `POST transactions should return 400 when description is blank`() {
        val requestBody = mapOf(
            "date" to "2026-04-30",
            "description" to "",
            "reference" to "TXN-001",
            "entries" to listOf(
                mapOf("accountId" to 1, "amount" to 500.00, "type" to "DEBIT"),
                mapOf("accountId" to 2, "amount" to 500.00, "type" to "CREDIT")
            )
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("Validation Failed"))
    }

    @Test
    fun `POST transactions should return 400 when debits do not equal credits`() {
        whenever(ledgerService.postTransaction(any(), any(), any(), any()))
            .thenThrow(IllegalArgumentException(
                "Debits (500.00) must equal credits (300.00)"
            ))

        val requestBody = mapOf(
            "date" to "2026-04-30",
            "description" to "Bad transaction",
            "reference" to "TXN-BAD",
            "entries" to listOf(
                mapOf("accountId" to 1, "amount" to 500.00, "type" to "DEBIT"),
                mapOf("accountId" to 2, "amount" to 300.00, "type" to "CREDIT")
            )
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message")
                .value("Debits (500.00) must equal credits (300.00)"))
    }

    @Test
    fun `POST transactions should return 400 when entries list is empty`() {
        val requestBody = mapOf(
            "date" to "2026-04-30",
            "description" to "Empty transaction",
            "reference" to "TXN-EMPTY",
            "entries" to emptyList<Any>()
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
    }

    @Test
    fun `POST transactions should return 400 when reference already exists`() {
        whenever(ledgerService.postTransaction(any(), any(), any(), any()))
            .thenThrow(IllegalArgumentException(
                "Transaction with reference 'TXN-001' already exists"
            ))

        val requestBody = mapOf(
            "date" to "2026-04-30",
            "description" to "Duplicate transaction",
            "reference" to "TXN-001",
            "entries" to listOf(
                mapOf("accountId" to 1, "amount" to 500.00, "type" to "DEBIT"),
                mapOf("accountId" to 2, "amount" to 500.00, "type" to "CREDIT")
            )
        )

        mockMvc.perform(
            post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message")
                .value("Transaction with reference 'TXN-001' already exists"))
    }

    @Test
    fun `GET transactions should return 200 with list of transactions`() {

        val transaction = Transaction(
            id = 1L,
            date = LocalDate.of(2026, 4, 23),
            description = "Received payment",
            reference = "TXN-001"

        )
        whenever(ledgerService.getAllTransactions())
            .thenReturn(listOf(transaction))

        mockMvc.perform(get("/api/transactions"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].reference").value("TXN-001"))

    }

    @Test
    fun `GET transactions by id should return 200 with transaction detail`() {
        val transaction = Transaction(
            id = 1L,
            date = LocalDate.of(2026, 4, 23),
            description = "Received payment",
            reference = "TXN-001"
        )
        whenever(ledgerService.getTransactionById(1L))
            .thenReturn(transaction)

        mockMvc.perform(get("/api/transactions/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.reference").value("TXN-001"))
    }

    @Test
    fun `GET transactions by id should return 400 when not found`() {
        whenever(ledgerService.getTransactionById(99L))
            .thenThrow(IllegalArgumentException("Transaction not found with id: 99"))

        mockMvc.perform(get("/api/transactions/99"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message")
                .value("Transaction not found with id: 99"))
    }
}