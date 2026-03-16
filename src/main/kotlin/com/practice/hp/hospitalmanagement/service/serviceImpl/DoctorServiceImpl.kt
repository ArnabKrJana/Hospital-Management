package com.practice.hp.hospitalmanagement.service.serviceImpl

import com.practice.hp.hospitalmanagement.entity.Appointment
import com.practice.hp.hospitalmanagement.repository.AppointmentRepository
import com.practice.hp.hospitalmanagement.repository.DoctorRepository
import com.practice.hp.hospitalmanagement.service.DoctorService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class DoctorServiceImpl(
    private val appointmentRepository: AppointmentRepository,
    private val doctorRepository: DoctorRepository
) : DoctorService {

    override fun getAllAppointments(pageable: Pageable,doctorId: UUID): Page<Appointment> {
        val doctor = doctorRepository.findById(doctorId).orElseThrow {
            NoSuchElementException("Doctor not found")
        }

        return appointmentRepository.findAllByDoctorId(pageable,doctor.id!!)
    }
}