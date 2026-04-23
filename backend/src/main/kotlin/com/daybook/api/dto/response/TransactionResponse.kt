package com.daybook.api.dto.response

import com.daybook.api.domain.model.Transaction
import java.time.LocalDate

data class TransactionResponse(
    val id: Long,
    val date: LocalDate,
    val description: String,
    val reference: String,
) {
    companion object {
        fun from(transaction: Transaction) =
            TransactionResponse(
                id = transaction.id!!,
                date = transaction.date,
                description = transaction.description,
                reference = transaction.reference,
            )
    }
}
