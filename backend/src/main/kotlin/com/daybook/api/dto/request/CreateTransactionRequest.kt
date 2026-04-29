package com.daybook.api.dto.request

import com.daybook.api.domain.enum.EntryType
import jakarta.validation.Valid
import jakarta.validation.constraints.*
import java.math.BigDecimal
import java.time.LocalDate

data class CreateTransactionRequest(
    @field:NotNull(message = "Date is required")
    val date: LocalDate,

    @field:NotBlank(message = "Description must not be blank")
    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String,

    @field:NotBlank(message = "Reference must not be blank")
    @field:Size(max = 50, message = "Reference must not exceed 50 characters")
    val reference: String,

    @field:NotEmpty(message = "At least one entry is required")
    @field:Valid
    val entries: List<EntryRequest>
) {
    data class EntryRequest(
        @field:NotNull(message = "Account ID is required")
        @field:Positive(message = "Account ID must be positive")
        val accountId: Long,

        @field:NotNull(message = "Amount is required")
        @field:DecimalMin(
            value = "0.01",
            message = "Amount must be greater than zero"
        )
        @field:Digits(
            integer = 15,
            fraction = 4,
            message = "Amount must have at most 15 integer digits and 4 decimal places"
        )
        val amount: BigDecimal,

        @field:NotNull(message = "Entry type is required")
        val type: EntryType
    )
}