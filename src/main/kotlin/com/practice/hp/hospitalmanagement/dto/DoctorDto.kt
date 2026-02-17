package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.Specialization
import java.util.UUID

data class DoctorDto(
    val id: UUID? = null,
    val fullName: String,
    val specialization: Specialization,
    val email: String,

    // Relationships
    val departmentIds: List<UUID> = emptyList(), // Many-to-Many link
    val headOfDepartmentId: UUID? = null // If they lead a department
)