package com.daybook.api.dto.request

import com.daybook.api.domain.enum.AccountType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateAccountRequest(
    @field:NotBlank(message = "Account name must not be blank")
    @field:Size(min = 2, max = 100, message = "Account name must be between 2 and 100 characters")
    val name: String,

    @field:NotNull(message = "Account type is required")
    val type: AccountType,

    @field:Size(max = 255, message = "Description must not exceed 255 characters")
    val description: String = ""
)