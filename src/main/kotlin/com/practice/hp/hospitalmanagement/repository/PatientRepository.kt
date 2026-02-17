package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Patient
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PatientRepository : JpaRepository<Patient, UUID> {
}