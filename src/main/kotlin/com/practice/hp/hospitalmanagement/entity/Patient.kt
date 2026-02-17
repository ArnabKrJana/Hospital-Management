package com.practice.hp.hospitalmanagement.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.*


@Entity
data class Patient(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID? = null,
    val firstName: String,
    val lastName: String,
    @Enumerated(EnumType.STRING)
    val gender: GenderType,
    val email: String,
    @CreationTimestamp
    val registrationDateTime: LocalDateTime?=null,
    @OneToMany(mappedBy = "patient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var appointments: List<Appointment> = emptyList(),

    @OneToOne(mappedBy = "patient", cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    var insurance: Insurance? = null,

    ) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Patient

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


enum class GenderType(val value: String) {
    MALE("Male"),
    FEMALE("Female"),
    OTHERS("Others")
}