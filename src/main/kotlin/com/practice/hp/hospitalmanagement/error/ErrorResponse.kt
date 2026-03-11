package com.practice.hp.hospitalmanagement.error

import java.time.LocalDateTime

data class ErrorResponse(
    val status: Int,
    val message: String?,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val path: String
)
