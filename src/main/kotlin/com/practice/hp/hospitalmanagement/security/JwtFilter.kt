package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Component
class JwtFilter(
    private val authUtil: AuthUtil,
    private val userRepository: UserRepository,
    @Qualifier("handlerExceptionResolver") private val resolver: HandlerExceptionResolver
) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        //extract header Authorization
        val header = request.getHeader("Authorization")
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }
        val token = header.substring("Bearer ".length)
        val username = authUtil.extractUsernameFromToken(token)
        userRepository.findByUsername(username)?.let {
            try {
                val isValid = authUtil.validateToken(it,token)
                if (isValid) log.info("Token validated: $token")
            } catch (e: Exception) {
                log.error(e) { "jwt validation failed with: $e" }
                SecurityContextHolder.clearContext()
                resolver.resolveException(request, response, null, e)
                return
            }

        } ?: throw UsernameNotFoundException("User not found")

        filterChain.doFilter(request, response)
    }

}