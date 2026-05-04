package com.daybook.api.controller

import com.daybook.api.config.TestSecurityConfig
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ConfigController::class)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig::class)
class ConfigControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var jwtService: com.daybook.api.security.JwtService

    @MockitoBean
    lateinit var daybookUserDetailsService:
            com.daybook.api.security.DaybookUserDetailsService

    @Test
    fun `GET config should return 200 with feature flags`() {
        mockMvc.perform(get("/api/config"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.version").value("1.0.0"))
            .andExpect(jsonPath("$.publicRegistrationEnabled").isBoolean)
    }
}