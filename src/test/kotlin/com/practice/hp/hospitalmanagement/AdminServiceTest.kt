package com.practice.hp.hospitalmanagement

import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.entity.Doctor
import com.practice.hp.hospitalmanagement.entity.Specialization
import com.practice.hp.hospitalmanagement.repository.DepartmentRepository
import com.practice.hp.hospitalmanagement.repository.DoctorRepository
import com.practice.hp.hospitalmanagement.service.AdminService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.util.*

@SpringBootTest
@Transactional // Rolls back changes after every test to keep DB clean
class AdminServiceTest @Autowired constructor(
    val adminService: AdminService,
    val doctorRepository: DoctorRepository,
    val departmentRepository: DepartmentRepository
) {

    // --- Helper Methods to Create Dummy Data ---

    private fun createDummyDoctor(name: String = "Dr. Strange", email: String = "strange@marvel.com"): Doctor {
        return Doctor(
            fullName = name,
            specialization = Specialization.NEUROLOGY,
            email = email
        )
    }

    private fun createDummyDepartmentDto(name: String, headId: UUID): DepartmentDto {
        return DepartmentDto(
            departmentName = name,
            departmentHeadId = headId
        )
    }

    // --- 1. Doctor Management Tests ---

    @Test
    fun `onboardDoctor should save doctor successfully`() {
        val doctor = createDummyDoctor()
        val savedDoctor = adminService.onboardDoctor(doctor)

        assertNotNull(savedDoctor.id)
        assertEquals("Dr. Strange", savedDoctor.fullName)
    }

    @Test
    fun `removeDoctor should delete doctor if they have no dependencies`() {
        val saved = adminService.onboardDoctor(createDummyDoctor())

        adminService.removeDoctor(saved.id!!)

        assertFalse(doctorRepository.existsById(saved.id!!))
    }

    // --- 2. Department Creation Tests ---

    @Test
    fun `createDepartment should succeed when head doctor exists`() {
        // 1. We need a doctor first (Pre-requisite)
        val headDoctor = adminService.onboardDoctor(createDummyDoctor())

        // 2. Create Dept DTO linked to that doctor
        val deptDto = createDummyDepartmentDto("Magic Dept", headDoctor.id!!)

        // 3. Act
        val savedDept = adminService.createDepartment(deptDto)

        // 4. Verify Dept
        assertNotNull(savedDept.id)
        assertEquals("Magic Dept", savedDept.departmentName)
        assertEquals(headDoctor.id, savedDept.departmentHead.id)

        // 5. Verify Inverse Relationship (Doctor is now Head)
        val updatedDoctor = doctorRepository.findById(headDoctor.id!!).get()
        assertNotNull(updatedDoctor.department)
        assertEquals(savedDept.id, updatedDoctor.department?.id)

        // 6. Verify Head is also a member (Many-to-Many)
        assertTrue(savedDept.doctors.any { it.id == headDoctor.id })
    }

    @Test
    fun `createDepartment should fail if department name is duplicate`() {
        // 1. Create first dept
        val doc1 = adminService.onboardDoctor(createDummyDoctor("Doc 1", "1@test.com"))
        adminService.createDepartment(createDummyDepartmentDto("Cardiology", doc1.id!!))

        // 2. Try creating second dept with same name
        val doc2 = adminService.onboardDoctor(createDummyDoctor("Doc 2", "2@test.com"))
        val duplicateDto = createDummyDepartmentDto("Cardiology", doc2.id!!) // Same Name!

        assertThrows<IllegalArgumentException> {
            adminService.createDepartment(duplicateDto)
        }
    }

    // --- 3. Association Tests (Assign/Remove) ---

    @Test
    fun `assignDoctorToDepartment should link both sides correctly`() {
        // 1. Setup: Create Dept (with Head) and a new Staff Doctor
        val head = adminService.onboardDoctor(createDummyDoctor("Head Doc", "head@test.com"))
        val dept = adminService.createDepartment(createDummyDepartmentDto("Ortho", head.id!!))

        val newStaff = adminService.onboardDoctor(createDummyDoctor("Staff Doc", "staff@test.com"))

        // 2. Act
        adminService.assignDoctorToDepartment(newStaff.id!!, dept.id!!)

        // 3. Verify Database State
        val updatedDept = departmentRepository.findById(dept.id!!).get()
        val updatedStaff = doctorRepository.findById(newStaff.id!!).get()

        // Check Owning Side (Dept has Staff)
        assertTrue(updatedDept.doctors.any { it.id == newStaff.id })

        // Check Inverse Side (Staff has Dept)
        assertTrue(updatedStaff.departments.any { it.id == dept.id })
    }

    @Test
    fun `removeDoctorFromDepartment should unlink but keep entities`() {
        // 1. Setup
        val head = adminService.onboardDoctor(createDummyDoctor("Head", "h@t.com"))
        val dept = adminService.createDepartment(createDummyDepartmentDto("Peds", head.id!!))
        val staff = adminService.onboardDoctor(createDummyDoctor("Staff", "s@t.com"))
        adminService.assignDoctorToDepartment(staff.id!!, dept.id!!)

        // 2. Act
        adminService.removeDoctorFromDepartment(staff.id!!, dept.id!!)

        // 3. Verify
        val updatedDept = departmentRepository.findById(dept.id!!).get()

        // Doctor should be gone from list
        assertFalse(updatedDept.doctors.any { it.id == staff.id })

        // But Doctor entity should still exist
        assertTrue(doctorRepository.existsById(staff.id!!))
    }

    // --- 4. Constraint & Logic Tests (The "Safety" Checks) ---

    @Test
    fun `removeDoctor should FAIL if doctor is a Department Head`() {
        // 1. Setup: Make a doctor a Department Head
        val head = adminService.onboardDoctor(createDummyDoctor())
        adminService.createDepartment(createDummyDepartmentDto("Important Dept", head.id!!))

        // 2. Act & Assert: Try to delete them
        val exception = assertThrows<IllegalStateException> {
            adminService.removeDoctor(head.id!!)
        }

        assertTrue(exception.message!!.contains("Cannot delete Doctor"))
        assertTrue(exception.message!!.contains("Head of Important Dept"))
    }

    @Test
    fun `removeDoctorFromDepartment should FAIL if doctor is the Department Head`() {
        // 1. Setup
        val head = adminService.onboardDoctor(createDummyDoctor())
        val dept = adminService.createDepartment(createDummyDepartmentDto("Radio", head.id!!))

        // 2. Act & Assert: Try to fire the boss from their own dept
        val exception = assertThrows<IllegalStateException> {
            adminService.removeDoctorFromDepartment(head.id!!, dept.id!!)
        }
        assertTrue(exception.message!!.contains("Cannot remove Doctor"))
    }

    @Test
    fun `assignDepartmentHead should swap heads correctly`() {
        // 1. Setup: Dept with Old Head
        val oldHead = adminService.onboardDoctor(createDummyDoctor("Old Boss", "old@t.com"))
        val dept = adminService.createDepartment(createDummyDepartmentDto("Surgery", oldHead.id!!))

        // 2. Setup: New Candidate
        val newHead = adminService.onboardDoctor(createDummyDoctor("New Boss", "new@t.com"))

        // 3. Act: Promote New Head
        adminService.assignDepartmentHead(newHead.id!!, dept.id!!)

        // 4. Verify
        val updatedDept = departmentRepository.findById(dept.id!!).get()
        val updatedOldHead = doctorRepository.findById(oldHead.id!!).get()
        val updatedNewHead = doctorRepository.findById(newHead.id!!).get()

        // Check Department
        assertEquals(newHead.id, updatedDept.departmentHead.id)

        // Check Old Head (Should be Demoted: department field null)
        assertNull(updatedOldHead.department)

        // Check New Head (Should be Promoted: department field set)
        assertEquals(dept.id, updatedNewHead.department?.id)

        // Check New Head is added to the doctor list
        assertTrue(updatedDept.doctors.any { it.id == newHead.id })
    }
}