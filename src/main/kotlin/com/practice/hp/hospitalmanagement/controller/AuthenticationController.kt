package com.practice.hp.hospitalmanagement.controller

import com.practice.hp.hospitalmanagement.dto.LoginRequestDto
import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.dto.SignUpRequestDto
import com.practice.hp.hospitalmanagement.dto.SignUpResponseDto
import com.practice.hp.hospitalmanagement.service.AuthenticationService
import com.practice.hp.hospitalmanagement.service.serviceImpl.AuthenticationServiceImpl

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthenticationController(
private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun login(@Valid @RequestBody loginRequest: LoginRequestDto): ResponseEntity<LoginResponseDto> {
       val response= authenticationService.login(loginRequest)
        return ResponseEntity.ok(response)
    }


    @PostMapping("/signup")
    fun signUp(@Valid @RequestBody signUpRequest: SignUpRequestDto): ResponseEntity<SignUpResponseDto>{
       return ResponseEntity.ok(authenticationService.signup(signUpRequest))
    }

}