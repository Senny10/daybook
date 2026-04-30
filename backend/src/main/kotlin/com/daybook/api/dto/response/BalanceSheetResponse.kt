package com.daybook.api.dto.response

import java.math.BigDecimal

data class BalanceSheetResponse(
    val totalAssets: BigDecimal,
    val totalLiabilities: BigDecimal,
    val totalEquity: BigDecimal,
    val isBalanced: Boolean
)