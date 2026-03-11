package com.practice.hp.hospitalmanagement.security.oAuth2

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper

@Component
class OAuth2SuccessHandler(
    val oAuth2Service: OAuth2Service,
    private val objectMapper: ObjectMapper
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val oAuth2Token = authentication as OAuth2AuthenticationToken
        val oAuth2User=oAuth2Token.principal!!
        val registrationId= oAuth2Token.authorizedClientRegistrationId

        // Get the LoginResponseDto (containing the JWT) from the service
        val loginResponse = oAuth2Service.handleOAuth2Login(oAuth2User)

        //  Write the DTO back to the client as a clean JSON response
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(objectMapper.writeValueAsString(loginResponse))
        response.writer.flush()
    }
}