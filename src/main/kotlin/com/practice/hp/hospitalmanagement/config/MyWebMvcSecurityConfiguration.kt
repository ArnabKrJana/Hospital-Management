package com.practice.hp.hospitalmanagement.config


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.security.autoconfigure.web.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class MyWebMvcSecurityConfiguration @Autowired constructor(
    private val appConfig: AppConfig
) {
    val bcryptEncoder = appConfig.provideBcryptEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/admin/departments").hasRole("ADMIN")
                    .requestMatchers("/admin/doctors").hasRole("DOCTOR")
                    .requestMatchers("/auth/**").permitAll()
                    .anyRequest().permitAll()
            }
            .formLogin{it.disable()}

        return http.build()
    }

    @Bean
    fun authenticationManager(
        authenticationConfiguration: AuthenticationConfiguration
    ): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

//    @Bean
//    fun myInMemoryUserDetailsManager(): InMemoryUserDetailsManager {
//        val user1 = User.withUsername("admin@gmail.com")
//            .password(bcryptEncoder.encode("password"))
//            .roles("ADMIN")
//            .build()
//        val user2=User.withUsername("doctor@gmail.com")
//            .password(bcryptEncoder.encode("kuchv")).roles("DOCTOR").build()
//        return InMemoryUserDetailsManager(user1,user2)
//    }


}
