package com.practice.hp.hospitalmanagement.util.mapper.mapperImpl

import com.practice.hp.hospitalmanagement.util.mapper.Mapper

import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.entity.Department
import org.springframework.stereotype.Component

@Component
class DepartmentMapper : Mapper<DepartmentDto, Department> {

    override fun dtoToEntity(dto: DepartmentDto): Department {
        // NOTE: Department requires a departmentHead (it's non-nullable in Entity).
        // This method might fail if you try to use it for Creation without a dummy doctor.
        // Usually, creation logic is handled manually in the Service for this specific entity.
        throw UnsupportedOperationException("Constructing Department from DTO is complex due to circular dependencies. Use AdminService.")
    }

    override fun entityToDto(entity: Department): DepartmentDto {
        return DepartmentDto(
            id = entity.id,
            departmentName = entity.departmentName,
            // Map the Head of Department ID
            departmentHeadId = entity.departmentHead.id,
            // Map the list of Doctor IDs
            doctorIds = entity.doctors.mapNotNull { it.id }
        )
    }


}