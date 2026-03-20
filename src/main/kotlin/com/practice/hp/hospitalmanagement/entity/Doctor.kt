package com.practice.hp.hospitalmanagement.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.MapsId
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import org.hibernate.proxy.HibernateProxy
import java.util.UUID

@Entity
data class Doctor(
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    @OneToOne
    @MapsId
    @JoinColumn("user_id")
    val user: User,
    val fullName: String,
    @Enumerated(EnumType.STRING)
    val specialization: Specialization,
    @Column(unique = true)
    val email: String,
    @OneToOne(mappedBy = "departmentHead")
    var department: Department? = null,
    @OneToMany(mappedBy = "doctor")
    var appointment: List<Appointment?> = emptyList(),

    @ManyToMany(mappedBy = "doctors", fetch = FetchType.LAZY)
    var departments: MutableSet<Department> = mutableSetOf()

) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Doctor

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

enum class Specialization(val value: String) {

    CARDIOLOGY("Cardiology"),
    NEUROLOGY("Neurology"),
    ORTHOPEDICS("Orthopedics"),
    PEDIATRICS("Pediatrics"),
    DERMATOLOGY("Dermatology"),
    GYNECOLOGY("Gynecology"),
    PSYCHIATRY("Psychiatry"),
    ONCOLOGY("Oncology"),
    RADIOLOGY("Radiology"),
    GENERAL_SURGERY("General Surgery");

}


