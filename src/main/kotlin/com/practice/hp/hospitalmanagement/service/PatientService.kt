package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.entity.Insurance
import com.practice.hp.hospitalmanagement.entity.Patient
import java.util.UUID

interface PatientService {

    // --- Patient Onboarding ---
    fun registerPatient(patient: Patient): Patient

    fun getPatientById(id: UUID): Patient

    fun getPatientByEmail(email: String): Patient?

    fun updatePatientDetails(id: UUID, patientDetails: Patient): Patient

    // Deletes patient AND cascades to delete their Appointments and Insurance
    fun deletePatient(id: UUID)

    // --- Insurance Management (OneToOne) ---
    // Since Insurance cannot exist without a Patient, we manage it here.

    // Creates or Updates the insurance for a specific patient
    fun addInsuranceToPatient(patientId: UUID, insurance: Insurance): Patient

    // Retrieves just the insurance policy for a patient
    fun getPatientInsurance(patientId: UUID): Insurance?

    // Removes insurance coverage (sets patient.insurance = null and deletes orphan)
    fun removePatientInsurance(patientId: UUID)
}