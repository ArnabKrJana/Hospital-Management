package com.practice.hp.hospitalmanagement.util.mapper

import com.practice.hp.hospitalmanagement.entity.User

interface Mapper<D, E> {
    fun dtoToEntity(dto: D,user: User): E
    fun entityToDto(entity: E): D
}