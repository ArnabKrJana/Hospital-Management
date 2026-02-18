package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.entity.Specialization

data class DoctorUpdateDto(
    val fullName: String? = null,
    val specialization: Specialization? = null,
    val email: String? = null
)