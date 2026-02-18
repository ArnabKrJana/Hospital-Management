package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.entity.GenderType

data class PatientUpdateDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val gender: GenderType? = null,
    val email: String? = null
)