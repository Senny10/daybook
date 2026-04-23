package com.daybook.api.dto.response

import java.math.BigDecimal

data class BalanceResponse(
    val accountId: Long,
    val accountName: String,
    val balance: BigDecimal,
)
