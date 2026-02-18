package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.entity.AppointmentCategory
import java.time.LocalDateTime
import java.util.UUID

data class AppointmentUpdateDto(
    val category: AppointmentCategory? = null,
    val reasonDescription: String? = null,
    val appointmentDateTime: LocalDateTime? = null,
    val doctorId: UUID? = null // Allows reassigning to a new doctor
)