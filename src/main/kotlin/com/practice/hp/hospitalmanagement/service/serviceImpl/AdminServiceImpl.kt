package com.practice.hp.hospitalmanagement.service.serviceImpl

import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.dto.DoctorDto
import com.practice.hp.hospitalmanagement.dto.updateDto.DoctorUpdateDto
import com.practice.hp.hospitalmanagement.entity.Department
import com.practice.hp.hospitalmanagement.entity.Doctor
import com.practice.hp.hospitalmanagement.repository.DepartmentRepository
import com.practice.hp.hospitalmanagement.repository.DoctorRepository
import com.practice.hp.hospitalmanagement.repository.UserRepository
import com.practice.hp.hospitalmanagement.service.AdminService
import com.practice.hp.hospitalmanagement.util.types.RoleType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class AdminServiceImpl(
    private val doctorRepository: DoctorRepository,
    private val departmentRepository: DepartmentRepository,
    private val userRepository: UserRepository
) : AdminService {

    @Transactional
    override fun upgradeUserToDoctor(userId: UUID, doctorDto: DoctorDto): Doctor {

        val targetUser = userRepository.findByIdOrNull(userId)
            ?: throw NoSuchElementException("User with id $userId not found")

        if (doctorRepository.existsById(userId)) {
            throw IllegalArgumentException("User is already a doctor")
        }

        // Upgrade roles
        targetUser.roles.add(RoleType.DOCTOR)
        userRepository.save(targetUser)

        // Create the Doctor entity linked to this specific user
        val newDoctor = Doctor(
            user = targetUser,
            fullName = requireNotNull(targetUser.name),
            specialization = doctorDto.specialization,
            email = doctorDto.email
        )

        return doctorRepository.save(newDoctor)
    }

    @Transactional(readOnly = true)
    override fun getDoctorById(id: UUID): Doctor {
        return doctorRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Doctor with id $id not found")
    }

    @Transactional(readOnly = true)
    override fun getAllDoctors(pageable: Pageable): Page<Doctor> {
        return doctorRepository.findAll(pageable)
    }

    @Transactional
    override fun updateDoctorProfile(id: UUID, doctorDetails: DoctorUpdateDto): Doctor {
        val existingDoctor = doctorRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Doctor with id $id not found")

        // --- 1. Handle Department Updates (Many-to-Many) ---
        // We only update this if the list is explicitly provided (not null).
        // If it's an empty list [], it will clear all departments.
        if (doctorDetails.departmentIds != null) {
            val newDepartmentIds = doctorDetails.departmentIds!!

            // Fetch the actual Department entities for the new IDs
            val newDepartments = departmentRepository.findAllById(newDepartmentIds).toMutableSet()

            // A. Remove Doctor from departments they are leaving
            // (Current Departments that are NOT in the New List)
            val departmentsToRemove = existingDoctor.departments.filter { !newDepartments.contains(it) }
            departmentsToRemove.forEach { dept ->
                // Department is the Owning Side, so we must remove from 'dept.doctors'
                dept.doctors.remove(existingDoctor)
            }

            // B. Add Doctor to departments they are joining
            // (New Departments that are NOT in the Current List)
            val departmentsToAdd = newDepartments.filter { !existingDoctor.departments.contains(it) }
            departmentsToAdd.forEach { dept ->
                // Department is the Owning Side, so we must add to 'dept.doctors'
                dept.doctors.add(existingDoctor)
            }

            // C. Update the Doctor's internal list so the returned object is correct
            // (This modifies the 'existingDoctor' reference before we copy it below)
            existingDoctor.departments = newDepartments
        }

        // --- 2. Update Scalar Fields & Save ---
        val updatedDoctor = existingDoctor.copy(
            fullName = doctorDetails.fullName ?: existingDoctor.fullName,
            specialization = doctorDetails.specialization ?: existingDoctor.specialization,
            email = doctorDetails.email ?: existingDoctor.email,

            // Keep the ID and other relationships as they are
            id = existingDoctor.id,
            department = existingDoctor.department,
            departments = existingDoctor.departments, // This now holds the updated set from above
            appointment = existingDoctor.appointment
        )

        return doctorRepository.save(updatedDoctor)
    }

    @Transactional
    override fun removeDoctor(id: UUID) {
        val doctor = doctorRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Doctor with id $id not found")

        // Constraint Check: Cannot delete a Department Head
        // If this doctor is a head of any department, we must block deletion or reassign first.
        if (doctor.department != null) {
            throw IllegalStateException("Cannot delete Doctor ${doctor.fullName} because they are the Head of ${doctor.department?.departmentName}. Assign a new Head first.")
        }

        // Cleanup Many-to-Many Associations (Doctor <-> Department)
        // We must remove this doctor from ALL department lists to clean up the Join Table
        doctor.departments.forEach { department ->
            department.doctors.remove(doctor)
            // No need to save department explicitly if CascadeType.MERGE/PERSIST is set,
            // but saving ensures the join table updates immediately.
            departmentRepository.save(department)
        }

        // Clear local collection to avoid Hibernate confusion
        doctor.departments.clear()

        doctorRepository.delete(doctor)
    }

    @Transactional
    override fun createDepartment(department: DepartmentDto): Department {
        //check if the department already exist
        department.id?.let { id ->
            if (departmentRepository.existsById(id)) {
                throw IllegalArgumentException("Department with id $id already exists")
            }
        }
        //checkByDepartmentName
        val formatedDeptName = department.departmentName.trim()
        if (departmentRepository.findByDepartmentName(formatedDeptName) != null) {
            throw IllegalArgumentException("Department $formatedDeptName already exists")
        }
//check the headDoctor id exists or not
        val headDoctor = doctorRepository.findByIdOrNull(department.departmentHeadId!!)
            ?: throw NoSuchElementException("Doctor with id ${department.departmentHeadId} not found")
        //create new department
        val newDepartment = Department(
            departmentName = department.departmentName,
            departmentHead = headDoctor,
            //initializing with the head doctor
            doctors = mutableSetOf(headDoctor)
        )
        // Update the Inverse Relationship (Doctor -> Department)
        headDoctor.department = newDepartment
        headDoctor.departments.add(newDepartment)

        return departmentRepository.save(newDepartment)
    }

    @Transactional(readOnly = true)
    override fun getDepartmentByName(name: String): Department? {
        return departmentRepository.findByDepartmentName(name)
            ?: throw NoSuchElementException("Department with name $name not found")
    }

    @Transactional(readOnly = true)
    override fun getAllDepartments(pageable: Pageable): Page<Department> {
        return departmentRepository.findAll(pageable)
    }

    @Transactional
    override fun assignDoctorToDepartment(doctorId: UUID, departmentId: UUID) {
        val doctor = doctorRepository.findByIdOrNull(doctorId)
            ?: throw NoSuchElementException("Doctor with id $doctorId not found")
        val department = departmentRepository.findByIdOrNull(departmentId)
            ?: throw NoSuchElementException("Department with id $departmentId not found")

        // 1. Owning Side Update (Department -> Doctors)
        department.doctors.add(doctor)

        // 2. Inverse Side Update (Doctor -> Departments)
        doctor.departments.add(department)

        // 3. Save (Cascade MERGE/PERSIST on Department handles the join table)
        departmentRepository.save(department)
    }

    @Transactional
    override fun removeDoctorFromDepartment(doctorId: UUID, departmentId: UUID) {
        val doctor = doctorRepository.findByIdOrNull(doctorId)
            ?: throw NoSuchElementException("Doctor with id $doctorId not found")
        val department = departmentRepository.findByIdOrNull(departmentId)
            ?: throw NoSuchElementException("Department with id $departmentId not found")

        // Constraint: Cannot remove the Department Head from their own department
        if (department.departmentHead.id == doctor.id) {
            throw IllegalStateException("Cannot remove Doctor ${doctor.fullName} from ${department.departmentName} because they are the Department Head.")
        }

        // Owning Side Remove
        department.doctors.remove(doctor)

        // Inverse Side Remove
        doctor.departments.remove(department)

        departmentRepository.save(department)
    }

    @Transactional
    override fun assignDepartmentHead(doctorId: UUID, departmentId: UUID): Department {
        val newHeadDoctor = doctorRepository.findByIdOrNull(doctorId)
            ?: throw NoSuchElementException("Doctor with id $doctorId not found")
        val department = departmentRepository.findByIdOrNull(departmentId)
            ?: throw NoSuchElementException("Department with id $departmentId not found")

        val oldHeadDoctor = department.departmentHead

        // Demote the Old Head
        // They are no longer head of this department (Inverse relationship nullified)
        oldHeadDoctor.department = null
        doctorRepository.save(oldHeadDoctor)

        // Promote the New Head
        department.departmentHead = newHeadDoctor
        newHeadDoctor.department = department // Inverse relationship set

        // Ensure New Head is also a member of the department
        if (!department.doctors.contains(newHeadDoctor)) {
            department.doctors.add(newHeadDoctor)
            newHeadDoctor.departments.add(department)
        }

        return departmentRepository.save(department)
    }
}