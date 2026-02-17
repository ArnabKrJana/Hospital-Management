package com.practice.hp.hospitalmanagement.util.mapper

interface Mapper<D, E> {
    fun dtoToEntity(dto: D): E
    fun entityToDto(entity: E): D
}