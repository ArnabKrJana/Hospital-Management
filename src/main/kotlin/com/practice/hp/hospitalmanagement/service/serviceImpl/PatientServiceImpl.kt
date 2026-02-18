package com.practice.hp.hospitalmanagement.service.serviceImpl

import com.practice.hp.hospitalmanagement.dto.updateDto.PatientUpdateDto
import com.practice.hp.hospitalmanagement.entity.Insurance
import com.practice.hp.hospitalmanagement.entity.Patient
import com.practice.hp.hospitalmanagement.repository.PatientRepository
import com.practice.hp.hospitalmanagement.service.PatientService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PatientServiceImpl(
    private val patientRepository: PatientRepository
) : PatientService {

    @Transactional
    override fun registerPatient(patient: Patient): Patient {
        if (patientRepository.findByEmail(patient.email) != null) {
            throw RuntimeException("Patient already exists with email: ${patient.email}")
        }
        return patientRepository.save(patient)
    }

    @Transactional(readOnly = true)
    override fun getPatientById(id: UUID): Patient {
        return patientRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Patient with id $id not found")
    }

    @Transactional(readOnly = true)
    override fun getPatientByEmail(email: String): Patient {
        return patientRepository.findByEmail(email)
            ?: throw NoSuchElementException("Patient with email $email not found")
    }

    @Transactional
    override fun updatePatientDetails(id: UUID, updateRequest: PatientUpdateDto): Patient {
        val existingPatient = patientRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Patient with id $id not found")

        val updatedPatient = existingPatient.copy(
            firstName = updateRequest.firstName ?: existingPatient.firstName,
            lastName = updateRequest.lastName ?: existingPatient.lastName,
            gender = updateRequest.gender ?: existingPatient.gender,
            email = updateRequest.email ?: existingPatient.email,

            id = existingPatient.id,
            registrationDateTime = existingPatient.registrationDateTime,

            appointments = existingPatient.appointments,
            insurance = existingPatient.insurance
        )

        return patientRepository.save(updatedPatient)
    }

    @Transactional
    override fun deletePatient(id: UUID) {
        if (!patientRepository.existsById(id)) {
            throw NoSuchElementException("Cannot delete. Patient with id $id not found")
        }

        patientRepository.deleteById(id)
    }

    @Transactional
    override fun addInsuranceToPatient(patientId: UUID, insurance: Insurance): Patient {
        val patient = patientRepository.findByIdOrNull(patientId)
            ?: throw NoSuchElementException("Patient with id $patientId not found")

        patient.insurance = insurance

        insurance.patient = patient

        return patientRepository.save(patient)
    }

    @Transactional(readOnly = true)
    override fun getPatientInsurance(patientId: UUID): Insurance? {
        val patient = patientRepository.findByIdOrNull(patientId)
            ?: throw NoSuchElementException("Patient with id $patientId not found")

        return patient.insurance
    }

    @Transactional
    override fun removePatientInsurance(patientId: UUID) {
        val patient = patientRepository.findByIdOrNull(patientId)
            ?: throw NoSuchElementException("Patient with id $patientId not found")

        patient.insurance = null

        patientRepository.save(patient)
    }
}