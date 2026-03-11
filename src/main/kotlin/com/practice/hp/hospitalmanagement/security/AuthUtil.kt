package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.UserRepository
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class AuthUtil(
    @Value($$"${jwt.secretkey}")
    private val securityKey: String,
    private val userRepository: UserRepository
) {

    fun getSecretKey(): SecretKey {
        return Keys.hmacShaKeyFor(securityKey.toByteArray())
    }

    fun generateJwtToken(user: User): String {
        val token = Jwts.builder()
            .subject(user.username)
            .claim("username", user.username)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date(System.currentTimeMillis() + 15 * 3600 * 1000))
            .signWith(getSecretKey())
            .compact()
        return token
    }

    fun extractUsernameFromToken(token: String): String {
        val claims = Jwts.parser().verifyWith(getSecretKey()).build()
        val username = claims.parseSignedClaims(token).payload.subject
        return username
    }

    fun validateToken(user: User, token: String): Boolean {
        val username = extractUsernameFromToken(token)
        if (SecurityContextHolder.getContext().authentication == null) {
            val authentication = UsernamePasswordAuthenticationToken(username, null, user.authorities)
            SecurityContextHolder.getContext().authentication = authentication
            return true
        }

        return false
    }
}