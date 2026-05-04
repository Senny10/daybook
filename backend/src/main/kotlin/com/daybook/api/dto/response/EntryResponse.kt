package com.daybook.api.dto.response

import com.daybook.api.domain.enum.EntryType
import com.daybook.api.domain.model.Entry
import java.math.BigDecimal

data class EntryResponse(
    val id: Long,
    val accountId: Long,
    val accountName: String,
    val amount: BigDecimal,
    val type: EntryType
) {
    companion object {
        fun from(entry: Entry) = EntryResponse(
            id = entry.id!!,
            accountId = entry.account.id!!,
            accountName = entry.account.name,
            amount = entry.amount,
            type = entry.type
        )
    }
}