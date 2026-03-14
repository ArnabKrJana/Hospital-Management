package com.practice.hp.hospitalmanagement.util.mapper.mapperImpl

import com.practice.hp.hospitalmanagement.dto.PatientDto
import com.practice.hp.hospitalmanagement.entity.Patient
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.util.mapper.Mapper

class PatientMapper : Mapper<PatientDto, Patient> {

    override fun dtoToEntity(dto: PatientDto, user: User): Patient {
        return Patient(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            gender = dto.gender,
            email = dto.email,
            user = user
        )
    }



    override fun entityToDto(entity: Patient): PatientDto {
        return PatientDto(
            id = entity.id,
            firstName = entity.firstName,
            lastName = entity.lastName,
            gender = entity.gender,
            email = entity.email,
            registrationDateTime = entity.registrationDateTime,
            insurance = entity.insurance?.let { InsuranceMapper().entityToDto(it) },
            appointmentIds = entity.appointments.mapNotNull { it.id }
        )
    }
}
