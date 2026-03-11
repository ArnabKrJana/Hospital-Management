package com.practice.hp.hospitalmanagement.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tools.jackson.databind.ObjectMapper

@Configuration
class ApplicationConfig {
    @Bean
    fun providesObjectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}