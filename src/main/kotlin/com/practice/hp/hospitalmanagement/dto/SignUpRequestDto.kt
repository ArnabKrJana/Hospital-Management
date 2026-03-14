package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.GenderType
import org.jetbrains.annotations.NotNull

data class SignUpRequestDto(
    @field:NotNull
    val username: String,
    @field:NotNull
    val password: String,
    @field:NotNull
    val firstName: String,
    @field:NotNull
    val lastName: String,
    @field:NotNull
    val gender: GenderType
)
