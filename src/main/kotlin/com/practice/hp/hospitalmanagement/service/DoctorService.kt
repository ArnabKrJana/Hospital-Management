package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.entity.Appointment
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface DoctorService {
    fun getAllAppointments(pageable: Pageable, doctorId: UUID): Page<Appointment>
}