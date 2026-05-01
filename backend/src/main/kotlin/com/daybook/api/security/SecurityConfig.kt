package com.daybook.api.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthenticationFilter,
    private val userDetailsService: DaybookUserDetailsService
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .securityContext { context ->
                context.securityContextRepository(
                    RequestAttributeSecurityContextRepository()
                )
            }
            .authorizeHttpRequests { auth ->
                auth
                    // Public endpoints — no auth required
                    .requestMatchers("/api/auth/**").permitAll()

                    // ADMIN only — write operations
                    .requestMatchers(HttpMethod.POST, "/api/accounts")
                    .hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/api/transactions")
                    .hasRole("ADMIN")

                    // USER and ADMIN — read operations
                    .requestMatchers(HttpMethod.GET, "/api/**")
                    .hasAnyRole("USER", "ADMIN")

                    // Everything else requires authentication
                    .anyRequest().authenticated()
            }
            .exceptionHandling { exceptions ->
                exceptions.authenticationEntryPoint { _, response, _ ->
                    response.status = HttpServletResponse.SC_UNAUTHORIZED
                    response.writer.write("Unauthorized — please provide a valid JWT token")
                }
            }
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Suppress("DEPRECATION")
    @Bean
    fun authenticationProvider(): AuthenticationProvider =
        DaoAuthenticationProvider(passwordEncoder()).also {
            it.setUserDetailsService(userDetailsService)
        }

    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager = config.authenticationManager
}