package com.practice.hp.hospitalmanagement.error

import mu.KotlinLogging
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val log= KotlinLogging.logger { }
}