package com.practice.hp.hospitalmanagement.dto.updateDto

import java.util.*

data class DepartmentUpdateDto(
    val departmentName: String? = null,
    val departmentHeadId: UUID? = null
)