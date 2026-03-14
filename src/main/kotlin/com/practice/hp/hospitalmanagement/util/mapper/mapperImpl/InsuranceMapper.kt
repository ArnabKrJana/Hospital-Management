package com.practice.hp.hospitalmanagement.util.mapper.mapperImpl

import com.practice.hp.hospitalmanagement.dto.InsuranceDto
import com.practice.hp.hospitalmanagement.entity.Insurance
import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.util.mapper.Mapper
import org.springframework.stereotype.Component

@Component
class InsuranceMapper : Mapper<InsuranceDto, Insurance> {

    override fun dtoToEntity(dto: InsuranceDto,user: User): Insurance {
        return Insurance(
            id = dto.id,
            provider = dto.provider,
            policyNumber = dto.policyNumber,
            issued = dto.issued,
            insuranceDate = dto.insuranceDate
            // Patient relationship is set in the Service
        )
    }

    override fun entityToDto(entity: Insurance): InsuranceDto {
        return InsuranceDto(
            id = entity.id,
            provider = entity.provider,
            policyNumber = entity.policyNumber,
            issued = entity.issued,
            insuranceDate = entity.insuranceDate,
            createdAt = entity.createdAt,
            patientId = entity.patient?.id
        )
    }

}