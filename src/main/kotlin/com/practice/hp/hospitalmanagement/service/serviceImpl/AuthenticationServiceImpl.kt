package com.practice.hp.hospitalmanagement.service.serviceImpl

import com.practice.hp.hospitalmanagement.dto.LoginRequestDto
import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.dto.SignUpRequestDto
import com.practice.hp.hospitalmanagement.dto.SignUpResponseDto
import com.practice.hp.hospitalmanagement.entity.Patient
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.PatientRepository
import com.practice.hp.hospitalmanagement.repository.UserRepository
import com.practice.hp.hospitalmanagement.security.AuthUtil
import com.practice.hp.hospitalmanagement.service.AuthenticationService
import com.practice.hp.hospitalmanagement.util.types.RoleType
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class AuthenticationServiceImpl(
    private val userRepository: UserRepository,
    private val patientRepository: PatientRepository,
    private val passwordEncoder: BCryptPasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val authUtil: AuthUtil
) : AuthenticationService {

    @Transactional
    override fun signup(signupCredentials: SignUpRequestDto): SignUpResponseDto {
        if (userRepository.findByUsername(signupCredentials.username) != null) {
            throw IllegalArgumentException("User already exists by username: ${signupCredentials.username}")
        }
        val newUser = User(
            username = signupCredentials.username,
            password = passwordEncoder.encode(signupCredentials.password),
            email = signupCredentials.username,
            name = "${signupCredentials.firstName} ${signupCredentials.lastName}",
            roles = mutableSetOf(RoleType.PATIENT)
        )
        val savedUser = userRepository.save(newUser)
        val newPatient = Patient(
            firstName = signupCredentials.firstName,
            lastName = signupCredentials.lastName,
            gender = signupCredentials.gender,
            email = signupCredentials.username,
            user = savedUser
        )
        patientRepository.save(newPatient)

        return SignUpResponseDto(
            userId = newUser.userId,
            userName = newUser.username,
        )
    }

    override fun login(loginCredentials: LoginRequestDto): LoginResponseDto {
        //check if the user is authenticated
        if (userRepository.findByUsername(loginCredentials.username) == null) {
            throw UsernameNotFoundException("Username not found: ${loginCredentials.username}")
        }
        val authenticatedUser = validateUser(loginCredentials)
        //generate access token and return
        val jwtToken = authUtil.generateJwtToken(authenticatedUser)
        return LoginResponseDto(
            jwt = jwtToken,
            userId = authenticatedUser.userId
        )
    }

    override fun validateUser(loginCredentials: LoginRequestDto): User {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginCredentials.username,
                loginCredentials.password
            )
        )
        val user = authentication.principal as User
        return user
    }
}