package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Appointment
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID


interface AppointmentRepository : JpaRepository<Appointment, UUID> {
}