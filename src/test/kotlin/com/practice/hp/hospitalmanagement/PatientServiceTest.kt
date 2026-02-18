package com.practice.hp.hospitalmanagement

import com.practice.hp.hospitalmanagement.dto.updateDto.PatientUpdateDto
import com.practice.hp.hospitalmanagement.entity.GenderType
import com.practice.hp.hospitalmanagement.entity.Insurance
import com.practice.hp.hospitalmanagement.entity.InsuranceProvider
import com.practice.hp.hospitalmanagement.entity.Patient
import com.practice.hp.hospitalmanagement.repository.PatientRepository
import com.practice.hp.hospitalmanagement.service.PatientService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional // Rolls back DB changes after each test
class PatientServiceTest @Autowired constructor(
    val patientService: PatientService,
    val patientRepository: PatientRepository // Injected to verify DB state directly
) {

    // --- Helper to create dummy data ---
    private fun createDummyPatient(email: String = "test@example.com"): Patient {
        return Patient(
            firstName = "John",
            lastName = "Doe",
            gender = GenderType.MALE,
            email = email
        )
    }

    // --- 1. Registration Tests ---

    @Test
    fun `registerPatient should save patient and generate ID`() {
        val patient = createDummyPatient()

        val savedPatient = patientService.registerPatient(patient)

        assertNotNull(savedPatient.id, "ID should be generated")
        assertEquals("test@example.com", savedPatient.email)

        // Verify in DB
        val inDb = patientRepository.findById(savedPatient.id!!).get()
        assertEquals("John", inDb.firstName)
    }

    @Test
    fun `registerPatient should throw exception for duplicate email`() {
        // 1. Save first patient
        patientService.registerPatient(createDummyPatient("duplicate@test.com"))

        // 2. Try to save second patient with same email
        val duplicatePatient = createDummyPatient("duplicate@test.com")

        val exception = assertThrows<RuntimeException> {
            patientService.registerPatient(duplicatePatient)
        }
        assertTrue(exception.message!!.contains("already exists"))
    }

    // --- 2. Retrieval Tests ---

    @Test
    fun `getPatientById should return correct patient`() {
        val saved = patientService.registerPatient(createDummyPatient())

        val found = patientService.getPatientById(saved.id!!)

        assertEquals(saved.id, found.id)
    }

    @Test
    fun `getPatientById should throw exception when not found`() {
        assertThrows<NoSuchElementException> {
            patientService.getPatientById(UUID.randomUUID())
        }
    }

    // --- 3. Update Tests (The Partial Update Logic) ---

    @Test
    fun `updatePatientDetails should update only specific fields`() {
        // 1. Setup
        val original = patientService.registerPatient(createDummyPatient())

        // 2. Create Update DTO (Only changing FirstName and Email)
        val updateDto = PatientUpdateDto(
            firstName = "Jane", // Changed
            lastName = null,    // Should remain "Doe"
            email = "jane@new.com" // Changed
        )

        // 3. Act
        val updated = patientService.updatePatientDetails(original.id!!, updateDto)

        // 4. Assert Return Value
        assertEquals("Jane", updated.firstName)
        assertEquals("Doe", updated.lastName) // Should stay same
        assertEquals("jane@new.com", updated.email)

        // 5. Assert DB State
        val inDb = patientRepository.findById(original.id!!).get()
        assertEquals("Jane", inDb.firstName)
    }

    // --- 4. Deletion Tests ---

    @Test
    fun `deletePatient should remove patient from DB`() {
        val saved = patientService.registerPatient(createDummyPatient())

        patientService.deletePatient(saved.id!!)

        assertFalse(patientRepository.existsById(saved.id!!))
    }

    // --- 5. Insurance Logic Tests ---

    @Test
    fun `addInsuranceToPatient should link insurance to patient`() {
        // 1. Setup Patient
        val patient = patientService.registerPatient(createDummyPatient())

        // 2. Create Insurance
        val insurance = Insurance(
            provider = InsuranceProvider.HDFC,
            policyNumber = 123456789,
            issued = true,
            insuranceDate = Date()
        )

        // 3. Act
        patientService.addInsuranceToPatient(patient.id!!, insurance)

        // 4. Verify
        val patientInDb = patientRepository.findById(patient.id!!).get()
        assertNotNull(patientInDb.insurance)
        assertEquals(123456789, patientInDb.insurance?.policyNumber)
        // Check bidirectional link
        assertEquals(patient.id, patientInDb.insurance?.patient?.id)
    }

    @Test
    fun `removePatientInsurance should delete insurance but keep patient`() {
        // 1. Setup Patient with Insurance
        val patient = patientService.registerPatient(createDummyPatient())
        val insurance = Insurance(
            provider = InsuranceProvider.LIC,
            policyNumber = 999,
            issued = true,
            insuranceDate = Date()
        )
        patientService.addInsuranceToPatient(patient.id!!, insurance)

        // 2. Act: Remove Insurance
        patientService.removePatientInsurance(patient.id!!)

        // 3. Verify
        val patientInDb = patientRepository.findById(patient.id!!).get()

        // Patient should still exist
        assertNotNull(patientInDb)
        // Insurance should be gone
        assertNull(patientInDb.insurance)
    }
}