package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.entity.DepartmentName
import java.util.UUID

data class DepartmentUpdateDto(
    val departmentName: DepartmentName? = null,
    val departmentHeadId: UUID? = null
)