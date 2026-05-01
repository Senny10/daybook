package com.daybook.api.dto.response

data class AuthResponse(
    val token: String,
    val username: String,
    val role: String
)