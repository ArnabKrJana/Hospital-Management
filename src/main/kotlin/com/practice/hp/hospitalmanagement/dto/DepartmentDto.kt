package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.DepartmentName
import java.util.UUID

data class DepartmentDto(
    val id: UUID? = null,
    val departmentName: DepartmentName,

    // Relationships
    val departmentHeadId: UUID? = null, // The doctor who leads this
    val doctorIds: List<UUID> = emptyList() // All doctors in this department
)