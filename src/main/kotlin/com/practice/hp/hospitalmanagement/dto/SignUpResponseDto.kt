package com.practice.hp.hospitalmanagement.dto

import java.util.UUID

data class SignUpResponseDto (
    val userId: UUID?=null,
    val userName: String
)