package com.practice.hp.hospitalmanagement.dto

import com.practice.hp.hospitalmanagement.entity.InsuranceProvider
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

data class InsuranceDto(
    val id: UUID? = null,
    val provider: InsuranceProvider,
    val policyNumber: Long,
    val issued: Boolean,
    val insuranceDate: Date,
    val createdAt: LocalDateTime? = null,

    // Relationship
    val patientId: UUID? = null // Link back to patient
)