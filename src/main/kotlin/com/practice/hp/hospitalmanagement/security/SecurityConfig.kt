package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.security.oAuth2.OAuth2SuccessHandler
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig (
    private val jwtFilter: JwtFilter,
    private val oAuth2SuccessHandler: OAuth2SuccessHandler,
    private val customAuthenticationEntryPoint:CustomAuthenticationEntryPoint,
    private val customAccessDeniedHandler: CustomAccessDeniedHandler
){

    @Bean
    fun providesPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun providesAuthenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun providesSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { SessionCreationPolicy.STATELESS }
            .authorizeHttpRequests { auth ->
                auth.requestMatchers("/admin/**").hasRole("ADMIN")
                auth.requestMatchers("/doctors/**").hasAnyRole("DOCTOR","ADMIN")
                auth.requestMatchers("/auth/**").permitAll()
                auth.requestMatchers("/oauth2/**").permitAll()
                auth.requestMatchers("/login/oauth2/code/**").permitAll()
                auth.anyRequest().authenticated()
            }.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter::class.java)
            .oauth2Login { oAuth2->
                oAuth2.failureHandler { request, response, exception ->
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized")
                }
                oAuth2.successHandler(oAuth2SuccessHandler)
            }.exceptionHandling {exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.authenticationEntryPoint(customAuthenticationEntryPoint)
                exceptionHandlingConfigurer.accessDeniedHandler(customAccessDeniedHandler)

            }
            .formLogin { it.disable() }
        return http.build()
    }
}