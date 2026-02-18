package com.practice.hp.hospitalmanagement.service.serviceImpl

import com.practice.hp.hospitalmanagement.dto.DepartmentDto
import com.practice.hp.hospitalmanagement.entity.Department
import com.practice.hp.hospitalmanagement.entity.Doctor
import com.practice.hp.hospitalmanagement.repository.DepartmentRepository
import com.practice.hp.hospitalmanagement.repository.DoctorRepository
import com.practice.hp.hospitalmanagement.service.AdminService
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
) : AdminService {

    @Transactional
    override fun onboardDoctor(doctor: Doctor): Doctor {
        doctor.id?.let { id ->
            if (doctorRepository.existsById(id)) {
                throw IllegalArgumentException("Doctor with id $id already exists")
            }
        }
        return doctorRepository.save(doctor)
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
    override fun updateDoctorProfile(id: UUID, doctorDetails: Doctor): Doctor {
        val existingDoctor = doctorRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("Doctor with id $id not found")

        // Standard Update Logic: Copy new values into the managed entity
        val updatedDoctor = existingDoctor.copy(
            fullName = doctorDetails.fullName,
            specialization = doctorDetails.specialization,
            email = doctorDetails.email,

            id = existingDoctor.id,
            department = existingDoctor.department,
            departments = existingDoctor.departments,
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