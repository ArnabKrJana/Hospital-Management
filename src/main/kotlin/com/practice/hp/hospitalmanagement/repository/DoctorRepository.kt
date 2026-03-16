package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Doctor
import org.springframework.data.domain.Page
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface DoctorRepository : JpaRepository<Doctor, UUID> {
}