package com.practice.hp.hospitalmanagement.config



import com.practice.hp.hospitalmanagement.entity.User
import com.practice.hp.hospitalmanagement.repository.UserRepository
import com.practice.hp.hospitalmanagement.util.types.RoleType
import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class AdminInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: BCryptPasswordEncoder
) : CommandLineRunner {

    override fun run(vararg args: String) {
        // Check if our default admin already exists
        if (userRepository.findByUsername("admin@hospital.com") == null) {

            // Create a pure User entity with the ADMIN role
            val adminUser = User(
                username = "admin@hospital.com",
                email = "admin@hospital.com",
                password = passwordEncoder.encode("admin123"), // Properly hashed!
                name = "System Admin",
                roles = mutableSetOf(RoleType.ADMIN)
            )

            userRepository.save(adminUser)
            println("✅ Default Admin created! Login -> admin@hospital.com / admin123")
        }
    }
}