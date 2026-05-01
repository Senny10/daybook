package com.daybook.api.controller

import com.daybook.api.dto.response.BalanceSheetResponse
import com.daybook.api.dto.response.ProfitAndLossResponse
import com.daybook.api.dto.response.TrialBalanceResponse
import com.daybook.api.service.ReportingService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.math.BigDecimal

@WebMvcTest(ReportingController::class)
class ReportingControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var reportingService: ReportingService

    @Test
    fun `GET trial-balance should return 200 with balanced report`() {
        val response = TrialBalanceResponse(
            entries = listOf(
                TrialBalanceResponse.TrialBalanceEntry(
                    accountId = 1L,
                    accountName = "Cash",
                    accountType = "ASSET",
                    debitBalance = BigDecimal("500.00"),
                    creditBalance = BigDecimal.ZERO
                ),
                TrialBalanceResponse.TrialBalanceEntry(
                    accountId = 2L,
                    accountName = "Revenue",
                    accountType = "REVENUE",
                    debitBalance = BigDecimal.ZERO,
                    creditBalance = BigDecimal("500.00")
                )
            ),
            totalDebits = BigDecimal("500.00"),
            totalCredits = BigDecimal("500.00"),
            isBalanced = true
        )
        whenever(reportingService.getTrialBalance()).thenReturn(response)

        mockMvc.perform(get("/api/reports/trial-balance"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isBalanced").value(true))
            .andExpect(jsonPath("$.totalDebits").value(500.00))
            .andExpect(jsonPath("$.totalCredits").value(500.00))
            .andExpect(jsonPath("$.entries.length()").value(2))
    }

    @Test
    fun `GET profit-and-loss should return 200 with P&L report`() {
        val response = ProfitAndLossResponse(
            totalRevenue = BigDecimal("500.00"),
            totalExpenses = BigDecimal.ZERO,
            netIncome = BigDecimal("500.00"),
            isProfit = true
        )
        whenever(reportingService.getProfitAndLoss()).thenReturn(response)

        mockMvc.perform(get("/api/reports/profit-and-loss"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.isProfit").value(true))
            .andExpect(jsonPath("$.totalRevenue").value(500.00))
            .andExpect(jsonPath("$.netIncome").value(500.00))
    }

    @Test
    fun `GET balance-sheet should return 200 with balance sheet`() {
        val response = BalanceSheetResponse(
            totalAssets = BigDecimal("500.00"),
            totalLiabilities = BigDecimal.ZERO,
            totalEquity = BigDecimal.ZERO,
            isBalanced = false
        )
        whenever(reportingService.getBalanceSheet()).thenReturn(response)

        mockMvc.perform(get("/api/reports/balance-sheet"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.totalAssets").value(500.00))
            .andExpect(jsonPath("$.isBalanced").value(false))
    }
}