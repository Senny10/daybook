package com.daybook.api.service

import com.daybook.api.domain.model.User
import com.daybook.api.domain.model.UserRole
import com.daybook.api.dto.request.LoginRequest
import com.daybook.api.dto.request.RegisterRequest
import com.daybook.api.dto.response.AuthResponse
import com.daybook.api.repository.UserRepository
import com.daybook.api.security.JwtService
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {
    @Value("\${daybook.registration.public:false}")
    private var publicRegistrationEnabled: Boolean = false

    fun register(
        request: RegisterRequest,
        requesterRole: String? = null
    ): AuthResponse {
        // Enforce registration policy
        if (!publicRegistrationEnabled && requesterRole != "ADMIN") {
            throw org.springframework.security.access.AccessDeniedException(
                "Public registration is disabled. " +
                        "Only ADMINs can create users."
            )
        }

        if (userRepository.existsByUsername(request.username)) {
            throw IllegalArgumentException(
                "Username '${request.username}' already exists"
            )
        }

        val user = userRepository.save(
            User(
                username = request.username,
                password = passwordEncoder.encode(request.password),
                role = request.role
            )
        )

        val token = jwtService.generateToken(
            user.username,
            user.role.name
        )

        return AuthResponse(
            token = token,
            username = user.username,
            role = user.role.name
        )
    }

    fun login(request: LoginRequest): AuthResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.username,
                request.password
            )
        )

        val user = userRepository.findByUsername(request.username)
            ?: throw IllegalArgumentException("User not found")

        val token = jwtService.generateToken(
            user.username,
            user.role.name
        )

        return AuthResponse(
            token = token,
            username = user.username,
            role = user.role.name
        )
    }
}