package com.practice.hp.hospitalmanagement.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne
import org.hibernate.engine.internal.Cascade
import org.hibernate.proxy.HibernateProxy
import java.util.UUID

@Entity
data class Department(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?=null,
    @Column(unique = true, nullable = false)
    var departmentName: String,
    @OneToOne
    @JoinColumn(name="doctor_department_head_id")
    var departmentHead: Doctor,

    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "doctor_department_table",
        joinColumns = [JoinColumn(name = "department_id")],
        inverseJoinColumns = [JoinColumn(name = "doctor_id")]
    )
    var doctors: MutableSet<Doctor> = mutableSetOf()

    ) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Department

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

