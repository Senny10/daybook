package com.daybook.api.controller

import com.daybook.api.config.TestSecurityConfig
import com.daybook.api.dto.response.AuthResponse
import com.daybook.api.service.AuthService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(AuthController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig::class)
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var authService: AuthService

    @MockitoBean
    lateinit var jwtService: com.daybook.api.security.JwtService

    @MockitoBean
    lateinit var daybookUserDetailsService:
            com.daybook.api.security.DaybookUserDetailsService

    @Test
    fun `POST register should return 201 with auth response`() {
        val authResponse = AuthResponse(
            token = "test.jwt.token",
            username = "newuser",
            role = "USER"
        )
        whenever(authService.register(any(), anyOrNull()))
            .thenReturn(authResponse)

        val requestBody = mapOf(
            "username" to "newuser",
            "password" to "password123!",
            "role" to "USER"
        )

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.username").value("newuser"))
            .andExpect(jsonPath("$.role").value("USER"))
            .andExpect(jsonPath("$.token").isNotEmpty)
    }

    @Test
    fun `POST register should return 400 when username is blank`() {
        val requestBody = mapOf(
            "username" to "",
            "password" to "password123!"
        )

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Validation Failed"))
    }

    @Test
    fun `POST register should return 400 when password too short`() {
        val requestBody = mapOf(
            "username" to "newuser",
            "password" to "short"
        )

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Validation Failed"))
    }

    @Test
    fun `POST register should return 400 when username already exists`() {
        whenever(authService.register(any(), anyOrNull()))
            .thenThrow(IllegalArgumentException(
                "Username 'existinguser' already exists"
            ))

        val requestBody = mapOf(
            "username" to "existinguser",
            "password" to "password123!"
        )

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message")
                .value("Username 'existinguser' already exists"))
    }

    @Test
    fun `POST register should return 403 when registration disabled`() {
        whenever(authService.register(any(), anyOrNull()))
            .thenThrow(
                org.springframework.security.access.AccessDeniedException(
                    "Public registration is disabled. " +
                            "Only ADMINs can create users."
                )
            )

        val requestBody = mapOf(
            "username" to "newuser",
            "password" to "password123!"
        )

        mockMvc.perform(
            post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isForbidden)
            .andExpect(jsonPath("$.status").value(403))
    }

    @Test
    fun `POST login should return 200 with JWT token`() {
        val authResponse = AuthResponse(
            token = "test.jwt.token",
            username = "admin",
            role = "ADMIN"
        )
        whenever(authService.login(any())).thenReturn(authResponse)

        val requestBody = mapOf(
            "username" to "admin",
            "password" to "admin123!"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.token").isNotEmpty)
            .andExpect(jsonPath("$.username").value("admin"))
            .andExpect(jsonPath("$.role").value("ADMIN"))
    }

    @Test
    fun `POST login should return 400 when username is blank`() {
        val requestBody = mapOf(
            "username" to "",
            "password" to "admin123!"
        )

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Validation Failed"))
    }
}