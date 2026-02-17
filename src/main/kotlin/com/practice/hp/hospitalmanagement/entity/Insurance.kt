package com.practice.hp.hospitalmanagement.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.proxy.HibernateProxy
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Entity
data class Insurance(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val id: UUID?=null,
    val provider:InsuranceProvider,
    val policyNumber:Long,
    val issued:Boolean,
    val insuranceDate: Date,
    @CreationTimestamp
    val createdAt: LocalDateTime?=null,
    @OneToOne
    @JoinColumn(name = "patient_insurance_id")
    var patient: Patient?=null,
) {
    final override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        val oEffectiveClass =
            if (other is HibernateProxy) other.hibernateLazyInitializer.persistentClass else other.javaClass
        val thisEffectiveClass =
            if (this is HibernateProxy) this.hibernateLazyInitializer.persistentClass else this.javaClass
        if (thisEffectiveClass != oEffectiveClass) return false
        other as Insurance

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

enum class InsuranceProvider(val value:String) {
    HDFC("hdfc"),
    ICICIC("icici"),
    LIC("lic")

}