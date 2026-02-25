package com.practice.hp.hospitalmanagement.dto

import java.util.UUID

data class LoginResponseDto(
    val jwt: String,
    val userId: UUID? = null
)
