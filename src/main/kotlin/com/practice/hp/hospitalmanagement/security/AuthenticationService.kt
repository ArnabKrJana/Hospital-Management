package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.dto.LoginRequestDto
import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.dto.SignUpRequestDto
import com.practice.hp.hospitalmanagement.dto.SignUpResponseDto
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val authenticationManager: AuthenticationManager,
    private val authUtil: AuthUtil,
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) {

    fun login(loginRequest: LoginRequestDto): LoginResponseDto {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )

        val user: User = authentication.principal as User
        val token = authUtil.generateAccessToken(user)
        return LoginResponseDto(token, user.userId)
    }

    fun signUp(signUpRequest: SignUpRequestDto): SignUpResponseDto {
        if (userRepository.findByUsername(signUpRequest.username) != null)
            throw IllegalArgumentException("Username ${signUpRequest.username} already exists")
        val encodedPassword =
            passwordEncoder.encode(signUpRequest.password) ?: throw IllegalArgumentException("Cannot be null")

        val user = userRepository.save(
            User(
                username = signUpRequest.username,
                password = encodedPassword
            )
        )
        return SignUpResponseDto(
            userId = user.userId,
            userName = user.username
        )
    }
}