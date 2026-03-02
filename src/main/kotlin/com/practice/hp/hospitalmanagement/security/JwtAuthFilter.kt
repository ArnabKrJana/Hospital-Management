package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.repository.UserRepository
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter


@Component
class JwtAuthFilter(
    private val userRepository: UserRepository,
    private val authUtil: AuthUtil

) : OncePerRequestFilter() {
    private val log = KotlinLogging.logger {}
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        log.info("hahaahah")

        val requestTokenHeader = request.getHeader("Authorization")
        // Authorization: Bearer sbdaskxdws.swbhdwud.ANJQOWSJK

        if (requestTokenHeader == null || !(requestTokenHeader.startsWith("Bearer "))) {
            filterChain.doFilter(request, response)
            return
        }
        val token = requestTokenHeader.substring(7)
        val username = authUtil.getUsernameFromToken(token)
        if (username != null && SecurityContextHolder.getContext().authentication == null) {
            val user = userRepository.findByUsername(username)
                ?: throw UsernameNotFoundException("User not found : $username")

            val authentication = UsernamePasswordAuthenticationToken(user, null, user.authorities)
            SecurityContextHolder.getContext().authentication=authentication

        }
        filterChain.doFilter(request,response)
    }
}