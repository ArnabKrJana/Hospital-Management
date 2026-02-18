package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Department
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DepartmentRepository : JpaRepository<Department, UUID> {
    fun findByDepartmentName(name: String): Department?
}