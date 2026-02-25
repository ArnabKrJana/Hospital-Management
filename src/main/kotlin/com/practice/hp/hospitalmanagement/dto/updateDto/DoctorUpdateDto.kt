package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.entity.Specialization
import java.util.UUID

data class DoctorUpdateDto(
    val fullName: String? = null,
    val specialization: Specialization? = null,
    val email: String? = null,
    val departmentIds: Set<UUID>? = null
)