package com.practice.hp.hospitalmanagement.dto.updateDto

import com.practice.hp.hospitalmanagement.entity.InsuranceProvider
import java.util.Date

data class InsuranceUpdateDto(
    val provider: InsuranceProvider? = null,
    val policyNumber: Long? = null,
    val issued: Boolean? = null,
    val insuranceDate: Date? = null
)