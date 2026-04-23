package com.daybook.api.dto.request

import com.daybook.api.domain.enum.EntryType
import java.math.BigDecimal
import java.time.LocalDate

data class CreateTransactionRequest(
    val date: LocalDate,
    val description: String,
    val reference: String,
    val entries: List<EntryRequest>,
) {
    data class EntryRequest(
        val accountId: Long,
        val amount: BigDecimal,
        val type: EntryType,
    )
}
