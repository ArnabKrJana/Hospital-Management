package com.practice.hp.hospitalmanagement.error

import jakarta.servlet.http.HttpServletRequest
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import io.jsonwebtoken.JwtException

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(UsernameNotFoundException::class)
    fun handleNotFound(ex: UsernameNotFoundException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.NOT_FOUND.value(),
            message = ex.message,
            path = request.requestURI
        )
        logger.error(error.message)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler(IllegalArgumentException::class, IllegalStateException::class)
    fun handleBadRequest(ex: RuntimeException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            message = ex.message,
            path = request.requestURI
        )
        logger.error(error.message)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }


    @ExceptionHandler(Exception::class)
    fun handleGlobalException(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            message = "An unexpected error occurred: ${ex.message}",
            path = request.requestURI
        )
        logger.error(error.message)
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: AuthenticationException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = ex.message ?: "Authentication failed",
            path = request.requestURI
        )
        logger.error { "Authentication error: ${error.message}" }
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(
        ex: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.FORBIDDEN.value(),
            message = ex.message ?: "You do not have permission to access this resource",
            path = request.requestURI
        )
        logger.error { "Access denied: ${error.message}" }
        return ResponseEntity(error, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(ex: JwtException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = HttpStatus.UNAUTHORIZED.value(),
            message = "Invalid or expired token",
            path = request.requestURI
        )
        logger.error { "JWT error: ${ex.message}" }
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }
}