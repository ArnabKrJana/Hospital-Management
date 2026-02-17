package com.practice.hp.hospitalmanagement.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Appointment(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?=null,
    @Enumerated(EnumType.STRING)
    val category: AppointmentCategory,

    @Column(length = 500)
    val reasonDescription: String,

    val appointmentDateTime: LocalDateTime,
    @CreationTimestamp
    val createdAt: LocalDateTime?=null,

    @ManyToOne
    @JoinColumn(name = "patient_appointment_id")
    var patient: Patient?=null,

    @ManyToOne
    @JoinColumn(name = "doctor_appointment_id")
    var doctor: Doctor?=null,

    ) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Appointment

        return id != null && id == other.id
    }

    final override fun hashCode(): Int =
        if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass.hashCode() else javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(" +
                "id = $id)"
    }
}

enum class AppointmentCategory(val value: String) {
    GENERAL_CHECKUP("general_checkup"),
    SPECIALIST_CONSULTATION("specialist_consultation"),
    EMERGENCY("emergency"),
    FOLLOW_UP("follow_up"),
    SURGERY("surfy"),
    DIAGNOSTIC_TEST("diagnostic_test"),
}
