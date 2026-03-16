package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

import java.util.UUID


interface AppointmentRepository : JpaRepository<Appointment, UUID> {
    fun findAllByDoctorId(pageable: Pageable, doctorId: UUID): Page<Appointment>
}