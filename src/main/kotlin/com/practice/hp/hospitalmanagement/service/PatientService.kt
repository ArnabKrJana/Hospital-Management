package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.dto.updateDto.PatientUpdateDto
import com.practice.hp.hospitalmanagement.entity.Insurance
import com.practice.hp.hospitalmanagement.entity.Patient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PatientService {

    fun getAllPatients(pageable: Pageable): Page<Patient>
    // --- Patient Onboarding ---
    fun registerPatient(patient: Patient): Patient

    fun getPatientById(id: UUID): Patient

    fun getPatientByEmail(email: String): Patient

    // Updated to accept DTO but return Entity (standard practice for internal service methods)
    fun updatePatientDetails(id: UUID, updateRequest: PatientUpdateDto): Patient

    // Deletes patient AND cascades to delete their Appointments and Insurance
    fun deletePatient(id: UUID)

    // --- Insurance Management (OneToOne) ---
    // Creates or Updates the insurance for a specific patient
    fun addInsuranceToPatient(patientId: UUID, insurance: Insurance): Patient

    // Retrieves just the insurance policy for a patient
    fun getPatientInsurance(patientId: UUID): Insurance?

    // Removes insurance coverage (sets patient.insurance = null and deletes orphan)
    fun removePatientInsurance(patientId: UUID)
}