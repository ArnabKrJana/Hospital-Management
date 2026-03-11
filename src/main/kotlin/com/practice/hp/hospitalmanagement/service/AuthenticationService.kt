package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.dto.LoginRequestDto
import com.practice.hp.hospitalmanagement.dto.LoginResponseDto
import com.practice.hp.hospitalmanagement.dto.SignUpRequestDto
import com.practice.hp.hospitalmanagement.dto.SignUpResponseDto
import com.practice.hp.hospitalmanagement.entity.User

interface AuthenticationService {
    fun login(loginCredentials: LoginRequestDto): LoginResponseDto
    fun signup(signupCredentials: SignUpRequestDto): SignUpResponseDto
    fun validateUser(loginCredentials: LoginRequestDto): User
}