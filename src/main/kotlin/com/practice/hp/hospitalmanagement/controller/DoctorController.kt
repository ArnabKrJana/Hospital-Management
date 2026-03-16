package com.practice.hp.hospitalmanagement.controller

import com.practice.hp.hospitalmanagement.dto.AppointmentDto
import com.practice.hp.hospitalmanagement.entity.Appointment
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.service.DoctorService
import com.practice.hp.hospitalmanagement.util.mapper.mapperImpl.AppointmentMapper
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/doctors")
class DoctorController(
    private val doctorService: DoctorService,
) {
    @GetMapping("/appointments")
    fun getAllAppointments(
        @PageableDefault()
        pageable: Pageable,
        @AuthenticationPrincipal currentUser: User
        ):ResponseEntity<Page<AppointmentDto>>{
        val doctorId = currentUser.userId!!

        val appointmentsPage = doctorService.getAllAppointments(pageable, doctorId)

        val dtoPage = appointmentsPage.map { AppointmentMapper().entityToDto(it) }

        return ResponseEntity.ok(dtoPage)
    }
}