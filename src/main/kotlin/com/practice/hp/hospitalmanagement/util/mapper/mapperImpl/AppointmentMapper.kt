package com.practice.hp.hospitalmanagement.util.mapper.mapperImpl

import com.practice.hp.hospitalmanagement.dto.AppointmentDto
import com.practice.hp.hospitalmanagement.entity.Appointment
import com.practice.hp.hospitalmanagement.util.mapper.Mapper

class AppointmentMapper : Mapper<AppointmentDto, Appointment> {

    override fun dtoToEntity(dto: AppointmentDto): Appointment {
        // Note: Cannot map 'patient' and 'doctor' objects here because we only have IDs.
        // The Service layer must fetch the Patient/Doctor entities and set them.
        return Appointment(
            id = dto.id,
            category = dto.category,
            reasonDescription = dto.reasonDescription,
            appointmentDateTime = dto.appointmentDateTime,
            // Relationships are left null intentionally; Service will fill them.
        )
    }

    override fun entityToDto(entity: Appointment): AppointmentDto {

        return AppointmentDto(
            id = entity.id,
            category = entity.category,
            reasonDescription = entity.reasonDescription,
            appointmentDateTime = entity.appointmentDateTime,
            createdAt = entity.createdAt,
            // Extract IDs safely
            patientId = entity.patient?.id ?: throw RuntimeException("Appointment must have a patient"),
            doctorId = entity.doctor?.id ?: throw RuntimeException("Appointment must have a doctor")
        )
    }
}