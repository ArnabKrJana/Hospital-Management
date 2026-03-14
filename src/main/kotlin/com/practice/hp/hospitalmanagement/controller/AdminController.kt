package com.practice.hp.hospitalmanagement.controller



import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.dto.DoctorDto
import com.practice.hp.hospitalmanagement.dto.updateDto.DoctorUpdateDto
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.service.AdminService
import com.practice.hp.hospitalmanagement.util.mapper.mapperImpl.DepartmentMapper
import com.practice.hp.hospitalmanagement.util.mapper.mapperImpl.DoctorMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import java.util.UUID

@RestController
@RequestMapping("/admin")
class AdminController(
    private val adminService: AdminService,
    private val doctorMapper: DoctorMapper,
    private val departmentMapper: DepartmentMapper
) {

    // ==========================================
    // DOCTOR MANAGEMENT
    // ==========================================

    @PostMapping("/doctors")
    fun onboardDoctor(@RequestBody doctorDto: DoctorDto,
                      @AuthenticationPrincipal currentUser: User
                      ): ResponseEntity<DoctorDto> {
        // 1. Convert DTO -> Entity
        val doctorEntity = doctorMapper.dtoToEntity(doctorDto,currentUser)

        // 2. Call Service
        val savedDoctor = adminService.onboardDoctor(doctorEntity)

        // 3. Convert Entity -> DTO & Return 201 Created
        return ResponseEntity(doctorMapper.entityToDto(savedDoctor), HttpStatus.CREATED)
    }

    @GetMapping("/doctors/{id}")
    fun getDoctorById(@PathVariable id: UUID): ResponseEntity<DoctorDto> {
        val doctor = adminService.getDoctorById(id)
        return ResponseEntity.ok(doctorMapper.entityToDto(doctor))
    }

    @GetMapping("/doctors")
    fun getAllDoctors(
        @PageableDefault(size = 10, sort = ["fullName"]) pageable: Pageable
    ): ResponseEntity<Page<DoctorDto>> {
        val doctorsPage = adminService.getAllDoctors(pageable)

        // Convert Page<Entity> to Page<DTO> using the mapper
        val dtoPage = doctorsPage.map { doctorMapper.entityToDto(it) }

        return ResponseEntity.ok(dtoPage)
    }

    @PutMapping("/doctors/{id}")
    fun updateDoctorProfile(
        @PathVariable id: UUID,
        @RequestBody doctorDto: DoctorUpdateDto
    ): ResponseEntity<DoctorDto> {
        val updatedDoctor = adminService.updateDoctorProfile(id, doctorDto)

        return ResponseEntity.ok(doctorMapper.entityToDto(updatedDoctor))
    }

    @DeleteMapping("/doctors/{id}")
    fun removeDoctor(@PathVariable id: UUID): ResponseEntity<Void> {
        adminService.removeDoctor(id)
        return ResponseEntity.noContent().build()
    }

    // ==========================================
    // DEPARTMENT MANAGEMENT
    // ==========================================

    @PostMapping("/departments")
    fun createDepartment(@RequestBody departmentDto: DepartmentDto): ResponseEntity<DepartmentDto> {
        // Service handles DTO directly for this complex logic (finding head doctor, etc.)
        val savedDepartment = adminService.createDepartment(departmentDto)
        return ResponseEntity(departmentMapper.entityToDto(savedDepartment), HttpStatus.CREATED)
    }

    @GetMapping("/departments/search")
    fun getDepartmentByName(@RequestParam name: String): ResponseEntity<DepartmentDto> {
        val department = adminService.getDepartmentByName(name)
        return if (department != null) {
            ResponseEntity.ok(departmentMapper.entityToDto(department))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping("/departments")
    fun getAllDepartments(
        @PageableDefault(size = 10, sort = ["departmentName"]) pageable: Pageable
    ): ResponseEntity<Page<DepartmentDto>> {
        val deptPage = adminService.getAllDepartments(pageable)
        val dtoPage = deptPage.map { departmentMapper.entityToDto(it) }
        return ResponseEntity.ok(dtoPage)
    }

    // ==========================================
    // ASSOCIATIONS (Many-to-Many & Head)
    // ==========================================

    @PostMapping("/departments/{deptId}/assign-doctor/{doctorId}")
    fun assignDoctorToDepartment(
        @PathVariable deptId: UUID,
        @PathVariable doctorId: UUID
    ): ResponseEntity<String> {
        adminService.assignDoctorToDepartment(doctorId, deptId)
        return ResponseEntity.ok("Doctor assigned to department successfully")
    }

    @DeleteMapping("/departments/{deptId}/remove-doctor/{doctorId}")
    fun removeDoctorFromDepartment(
        @PathVariable deptId: UUID,
        @PathVariable doctorId: UUID
    ): ResponseEntity<String> {
        adminService.removeDoctorFromDepartment(doctorId, deptId)
        return ResponseEntity.ok("Doctor removed from department successfully")
    }

    @PutMapping("/departments/{deptId}/assign-head/{doctorId}")
    fun assignDepartmentHead(
        @PathVariable deptId: UUID,
        @PathVariable doctorId: UUID
    ): ResponseEntity<DepartmentDto> {
        val updatedDept = adminService.assignDepartmentHead(doctorId, deptId)
        return ResponseEntity.ok(departmentMapper.entityToDto(updatedDept))
    }
}