package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.entity.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys.hmacShaKeyFor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class AuthUtil(
    @Value("\${jwt.secretkey}")
    private val jwtSecretKey: String
) {
    private fun getSecretKey(): SecretKey {
        return hmacShaKeyFor(jwtSecretKey.toByteArray())
    }

    fun generateAccessToken(user: User): String {
        return Jwts.builder()
            .subject(user.username)
            .claim("userId", user.userId)
            .issuedAt(Date.from(Instant.now()))
            .expiration(
                Date(System.currentTimeMillis() + 1000 * 60 * 10)
            ).
            signWith(getSecretKey())
            .compact()
    }
}