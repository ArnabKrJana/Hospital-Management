package com.practice.hp.hospitalmanagement.service

import com.practice.hp.hospitalmanagement.entity.Appointment
import com.practice.hp.hospitalmanagement.entity.AppointmentCategory
import java.util.UUID

interface AppointmentService {

    // --- Booking Logic ---
    // Note: We pass IDs because the appointment entity passed in usually
    // doesn't have the full Patient/Doctor objects attached yet.
    fun bookAppointment(patientId: UUID, doctorId: UUID, appointment: Appointment): Appointment

    // --- Modifications ---
    fun rescheduleAppointment(appointmentId: UUID, newDate: String): Appointment // or LocalDateTime

    fun cancelAppointment(appointmentId: UUID)

    fun updateAppointmentReason(appointmentId: UUID, newReason: String): Appointment

    // --- Retrieval / Reporting ---
    fun getAppointmentById(id: UUID): Appointment

    // Useful for the "Patient Dashboard"
    fun getAppointmentsForPatient(patientId: UUID): List<Appointment>

    // Useful for the "Doctor Dashboard" (See my schedule)
    fun getAppointmentsForDoctor(doctorId: UUID): List<Appointment>

    // Useful for "Filter by Category" (e.g., Show me all Surgeries)
    fun getAppointmentsByCategory(category: AppointmentCategory): List<Appointment>
}