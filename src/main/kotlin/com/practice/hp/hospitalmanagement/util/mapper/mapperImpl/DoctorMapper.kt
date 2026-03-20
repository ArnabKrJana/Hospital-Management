package com.practice.hp.hospitalmanagement.util.mapper.mapperImpl

import com.practice.hp.hospitalmanagement.dto.DoctorDto
import com.practice.hp.hospitalmanagement.entity.Doctor
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.util.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class DoctorMapper : Mapper<DoctorDto, Doctor> {

    override fun dtoToEntity(dto: DoctorDto,user: User): Doctor {
        return Doctor(
            id = dto.id,
            fullName = requireNotNull(dto.fullName),
            specialization = dto.specialization,
            email = dto.email,
            user = user
        )
    }

    override fun entityToDto(entity: Doctor): DoctorDto {
        return DoctorDto(
            id = entity.id,
            fullName = entity.fullName,
            specialization = entity.specialization,
            email = entity.email,
            // Extract Department IDs from the Many-to-Many set
            departmentIds = entity.departments.mapNotNull { it.id }.toList(),
            headOfDepartmentId = entity.department?.id // If they are a department head
        )
    }


}