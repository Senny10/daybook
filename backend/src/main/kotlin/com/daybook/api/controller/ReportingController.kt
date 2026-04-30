package com.daybook.api.controller

import com.daybook.api.dto.response.BalanceSheetResponse
import com.daybook.api.dto.response.ProfitAndLossResponse
import com.daybook.api.dto.response.TrialBalanceResponse
import com.daybook.api.service.ReportingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reports")
class ReportingController(
    private val reportingService: ReportingService
) {

    @GetMapping("/trial-balance")
    fun getTrialBalance(): TrialBalanceResponse =
        reportingService.getTrialBalance()

    @GetMapping("/profit-and-loss")
    fun getProfitAndLoss(): ProfitAndLossResponse =
        reportingService.getProfitAndLoss()

    @GetMapping("/balance-sheet")
    fun getBalanceSheet(): BalanceSheetResponse =
        reportingService.getBalanceSheet()
}