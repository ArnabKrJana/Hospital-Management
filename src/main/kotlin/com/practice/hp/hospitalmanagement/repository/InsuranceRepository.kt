package com.practice.hp.hospitalmanagement.repository

import com.practice.hp.hospitalmanagement.entity.Insurance
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface InsuranceRepository : JpaRepository<Insurance, UUID> {
}