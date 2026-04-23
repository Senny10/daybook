package com.daybook.api.dto.request

import com.daybook.api.domain.enum.AccountType

data class CreateAccountRequest(
    val name: String,
    val type: AccountType,
    val description: String = "",
)
