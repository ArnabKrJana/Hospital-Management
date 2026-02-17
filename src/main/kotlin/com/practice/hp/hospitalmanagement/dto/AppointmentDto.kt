package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.AppointmentCategory
import java.time.LocalDateTime
import java.util.UUID

data class AppointmentDto(
    val id: UUID? = null,
    val category: AppointmentCategory,
    val reasonDescription: String,
    val appointmentDateTime: LocalDateTime,
    val createdAt: LocalDateTime? = null,

    // Relationships (Crucial: Use IDs, not Objects)
    val patientId: UUID,
    val doctorId: UUID
)