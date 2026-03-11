package com.practice.hp.hospitalmanagement.security.oAuth2


import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.UserRepository
import com.practice.hp.hospitalmanagement.security.AuthUtil
import com.practice.hp.hospitalmanagement.util.OAuthProviderType
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service


@Service
class OAuth2Service(
    private val userRepository: UserRepository,
    private val authUtil: AuthUtil
) {

    fun handleOAuth2Login(oAuth2User: OAuth2User): LoginResponseDto {
        val providerId = oAuth2User.attributes["sub"].toString()
        val providerType = OAuthProviderType.GOOGLE
        val name = oAuth2User.attributes["name"].toString()
        val email = oAuth2User.attributes["email"].toString()

        var user = userRepository.findByUsername(email)

        if (user == null) {
            val newUser = User(
                username = email,
                providerId = providerId,
                providerType = providerType,
                email = email,
                name = name
            )
            user = userRepository.save(newUser)
        } else {
            val existingUser = User(
                username = email,
                providerId = providerId,
                providerType = providerType,
                email = email,
                name = name
            )
            user = userRepository.save(existingUser)
        }


        // Generate the JWT token
        val jwtToken = authUtil.generateJwtToken(user)

        return LoginResponseDto(
            jwt = jwtToken,
            userId = user.userId
        )
    }

}