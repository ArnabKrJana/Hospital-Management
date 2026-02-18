package com.practice.hp.hospitalmanagement.dto

import java.util.*

data class DepartmentDto(
    val id: UUID? = null,
    val departmentName: String,

    // Relationships
    val departmentHeadId: UUID? = null, // The doctor who leads this
    val doctorIds: List<UUID> = emptyList() // All doctors in this department
)