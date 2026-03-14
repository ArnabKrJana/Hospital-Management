package com.practice.hp.hospitalmanagement.security.oAuth2


import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.entity.GenderType
import com.practice.hp.hospitalmanagement.entity.Patient
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.PatientRepository
import com.practice.hp.hospitalmanagement.repository.UserRepository
import com.practice.hp.hospitalmanagement.security.AuthUtil
import com.practice.hp.hospitalmanagement.util.types.OAuthProviderType
import com.practice.hp.hospitalmanagement.util.types.RoleType
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class OAuth2Service(
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository,
    private val authUtil: AuthUtil
) {
    @Transactional
    fun handleOAuth2Login(oAuth2User: OAuth2User): LoginResponseDto {
        val providerId = oAuth2User.attributes["sub"].toString()
        val providerType = OAuthProviderType.GOOGLE
        val name = oAuth2User.attributes["name"].toString()
        val email = oAuth2User.attributes["email"].toString()
        val gender = oAuth2User.attributes["gender"].toString()
        var user = userRepository.findByUsername(email)

        if (user == null) {
            val newUser = User(
                username = email,
                providerId = providerId,
                providerType = providerType,
                email = email,
                name = name,
                roles = mutableSetOf(RoleType.PATIENT)
            )
            user = userRepository.save(newUser)

            val namePartsBySpace=name.split(" ")
            val firstName = namePartsBySpace.firstOrNull() ?: "Unknown"
            val lastName = if (namePartsBySpace.size > 1) namePartsBySpace.drop(1).joinToString(" ") else ""

            val newPatient = Patient(
                firstName = firstName,
                lastName = lastName,
                gender = GenderType.PREFERRED_NOT_SAY,
                email = email,
                user = user // Link via @MapsId
            )
            patientRepository.save(newPatient)
        } else {
            user.providerId = providerId
            user.providerType = providerType
            user.name = name
            user = userRepository.save(user)
        }


        // Generate the JWT token
        val jwtToken = authUtil.generateJwtToken(user)

        return LoginResponseDto(
            jwt = jwtToken,
            userId = user.userId
        )
    }

}