package com.daybook.api.dto.response

import java.math.BigDecimal

data class ProfitAndLossResponse(
    val totalRevenue: BigDecimal,
    val totalExpenses: BigDecimal,
    val netIncome: BigDecimal,
    val isProfit: Boolean
)