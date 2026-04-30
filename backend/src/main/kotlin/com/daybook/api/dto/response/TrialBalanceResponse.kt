package com.daybook.api.dto.response

import java.math.BigDecimal

data class TrialBalanceResponse(
    val entries: List<TrialBalanceEntry>,
    val totalDebits: BigDecimal,
    val totalCredits: BigDecimal,
    val isBalanced: Boolean
) {
    data class TrialBalanceEntry(
        val accountId: Long,
        val accountName: String,
        val accountType: String,
        val debitBalance: BigDecimal,
        val creditBalance: BigDecimal
    )
}