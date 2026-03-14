package com.practice.hp.hospitalmanagement.controller

import com.practice.hp.hospitalmanagement.dto.InsuranceDto
import com.practice.hp.hospitalmanagement.dto.PatientDto
import com.practice.hp.hospitalmanagement.dto.updateDto.PatientUpdateDto
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.service.PatientService
import com.practice.hp.hospitalmanagement.util.mapper.mapperImpl.InsuranceMapper
import com.practice.hp.hospitalmanagement.util.mapper.mapperImpl.PatientMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/patients")
class PatientController(
    private val patientService: PatientService,
    private val insuranceMapper: InsuranceMapper,

    ) {

    private val patientMapper = PatientMapper()

    // ==========================================
    // PATIENT CORE MANAGEMENT
    // ==========================================

    @PostMapping
    fun registerPatient(
        @RequestBody patientDto: PatientDto,
        @AuthenticationPrincipal currentUser: User
    ): ResponseEntity<PatientDto> {
        val patientEntity = patientMapper.dtoToEntity(patientDto,currentUser)
        val savedPatient = patientService.registerPatient(patientEntity)
        return ResponseEntity(patientMapper.entityToDto(savedPatient), HttpStatus.CREATED)
    }

    @GetMapping
    fun getAllPatients(
        @PageableDefault(size = 10, sort = ["firstName"]) pageable: Pageable
    ): ResponseEntity<Page<PatientDto>> {
        val patientsPage = patientService.getAllPatients(pageable)
        val dtoPage = patientsPage.map { patientMapper.entityToDto(it) }
        return ResponseEntity.ok(dtoPage)
    }

    @GetMapping("/{id}")
    fun getPatientById(@PathVariable id: UUID): ResponseEntity<PatientDto> {
        val patient = patientService.getPatientById(id)
        return ResponseEntity.ok(patientMapper.entityToDto(patient))
    }

    @GetMapping("/search")
    fun getPatientByEmail(@RequestParam email: String): ResponseEntity<PatientDto> {
        val patient = patientService.getPatientByEmail(email)
        return ResponseEntity.ok(patientMapper.entityToDto(patient))
    }

    @PutMapping("/{id}")
    fun updatePatientDetails(
        @PathVariable id: UUID,
        @RequestBody updateRequest: PatientUpdateDto
    ): ResponseEntity<PatientDto> {
        val updatedPatient = patientService.updatePatientDetails(id, updateRequest)
        return ResponseEntity.ok(patientMapper.entityToDto(updatedPatient))
    }

    @DeleteMapping("/{id}")
    fun deletePatient(@PathVariable id: UUID): ResponseEntity<Void> {
        patientService.deletePatient(id)
        return ResponseEntity.noContent().build()
    }

    // ==========================================
    // PATIENT INSURANCE MANAGEMENT
    // ==========================================

    @PostMapping("/{patientId}/insurance")
    fun addInsuranceToPatient(
        @PathVariable patientId: UUID,
        @RequestBody insuranceDto: InsuranceDto,
        @AuthenticationPrincipal currentUser: User
    ): ResponseEntity<PatientDto> {
        val insuranceEntity = insuranceMapper.dtoToEntity(insuranceDto,currentUser)
        val updatedPatient = patientService.addInsuranceToPatient(patientId, insuranceEntity)
        return ResponseEntity.ok(patientMapper.entityToDto(updatedPatient))
    }

    @GetMapping("/{patientId}/insurance")
    fun getPatientInsurance(@PathVariable patientId: UUID): ResponseEntity<InsuranceDto> {
        val insurance = patientService.getPatientInsurance(patientId)
        return if (insurance != null) {
            ResponseEntity.ok(insuranceMapper.entityToDto(insurance))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("/{patientId}/insurance")
    fun removePatientInsurance(@PathVariable patientId: UUID): ResponseEntity<Void> {
        patientService.removePatientInsurance(patientId)
        return ResponseEntity.noContent().build()
    }
}