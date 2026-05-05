package com.daybook.api.controller

import com.daybook.api.dto.request.LoginRequest
import com.daybook.api.dto.request.RegisterRequest
import com.daybook.api.dto.response.AuthResponse
import com.daybook.api.service.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
        @Valid @RequestBody request: RegisterRequest,
        authentication: org.springframework.security.core.Authentication?
    ): AuthResponse {
        // Extract role from JWT if authenticated, null if public request
        val requesterRole = authentication?.authorities
            ?.firstOrNull()
            ?.authority
            ?.removePrefix("ROLE_")

        return authService.register(request, requesterRole)
    }

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): AuthResponse = authService.login(request)
}