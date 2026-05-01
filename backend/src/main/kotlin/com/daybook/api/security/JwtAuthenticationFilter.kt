package com.daybook.api.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val userDetailsService: DaybookUserDetailsService
) : OncePerRequestFilter() {

    private val securityContextRepository =
        RequestAttributeSecurityContextRepository()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val jwt = authHeader.substring(7)

        val username = try {
            jwtService.extractUsername(jwt)
        } catch (e: Exception) {
            filterChain.doFilter(request, response)
            return
        }

        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(username)

            if (jwtService.isTokenValid(jwt, username)) {
                val role = jwtService.extractRole(jwt)

                val authToken = UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    listOf(SimpleGrantedAuthority("ROLE_$role"))
                )
                authToken.details = WebAuthenticationDetailsSource()
                    .buildDetails(request)

                val context = SecurityContextHolder.createEmptyContext()
                context.authentication = authToken
                SecurityContextHolder.setContext(context)

                // Save context so Spring Security 6 filter chain can read it
                securityContextRepository.saveContext(
                    context, request, response
                )
            }
        }

        filterChain.doFilter(request, response)
    }
}