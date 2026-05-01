package com.daybook.api.dto.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank(message = "Username must not be blank")
    val username: String,

    @field:NotBlank(message = "Password must not be blank")
    val password: String
)