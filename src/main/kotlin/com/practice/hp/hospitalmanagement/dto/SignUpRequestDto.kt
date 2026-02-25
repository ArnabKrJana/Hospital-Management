package com.practice.hp.hospitalmanagement.dto

import org.jetbrains.annotations.NotNull

data class SignUpRequestDto(
    @field:NotNull
    val username: String,
    @field:NotNull
    val password: String
)
