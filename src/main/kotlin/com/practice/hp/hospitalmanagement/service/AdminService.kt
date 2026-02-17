package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.entity.Department
import com.practice.hp.hospitalmanagement.entity.DepartmentName
import com.practice.hp.hospitalmanagement.entity.Doctor
import java.util.UUID

interface AdminService {

    //  Doctor Management
    fun onboardDoctor(doctor: Doctor): Doctor

    fun getDoctorById(id: UUID): Doctor

    fun getAllDoctors(): List<Doctor>

    // Updates details like email, name, or specialization
    fun updateDoctorProfile(id: UUID, doctorDetails: Doctor): Doctor

    // Hard delete (careful!) or soft delete logic
    fun removeDoctor(id: UUID)

    // Department Management
    fun createDepartment(department: Department): Department

    fun getDepartmentByName(name: DepartmentName): Department?

    fun getAllDepartments(): List<Department>

    //  The Association Logic (Crucial!)

    // 1. Assigns a doctor to a specific department (ManyToMany)
    // Implementation tip: Fetch both, add to sets, save.
    fun assignDoctorToDepartment(doctorId: UUID, departmentId: UUID)

    // 2. Removes a doctor from a specific department (ManyToMany)
    // Implementation tip: Fetch both, remove from sets, save.
    fun removeDoctorFromDepartment(doctorId: UUID, departmentId: UUID)

    // 3. Promotes a doctor to Head of Department (OneToOne)
    fun assignDepartmentHead(doctorId: UUID, departmentId: UUID): Department
}