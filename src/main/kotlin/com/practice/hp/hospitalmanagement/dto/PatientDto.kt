package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.GenderType
import java.time.LocalDateTime
import java.util.UUID

data class PatientDto(
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    val gender: GenderType,
    val email: String,
    val registrationDateTime: LocalDateTime? = null, // Nullable for request

    // Relationships
    val insurance: InsuranceDto? = null, // Nested DTO is okay for One-to-One
    val appointmentIds: List<UUID> = emptyList() // Just IDs to prevent recursion
)