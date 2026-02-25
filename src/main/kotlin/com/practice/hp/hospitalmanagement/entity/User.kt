package com.practice.hp.hospitalmanagement.entity


import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID

@Entity
@Table(name = "user_tbl")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    val userId: UUID? = null,

    @Column(unique = true, length = 64)
    private val username: String,

    private val password: String

) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> =
        emptyList()

    override fun getPassword(): String =
        password

    override fun getUsername(): String =
        username


}