package com.practice.hp.hospitalmanagement.entity


import com.practice.hp.hospitalmanagement.security.RoleToPermission
import com.practice.hp.hospitalmanagement.util.types.OAuthProviderType
import com.practice.hp.hospitalmanagement.util.types.RoleType
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.proxy.HibernateProxy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
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
    private val password: String? = null,
    @Column(unique = true) var providerId: String? = null,

    @Enumerated(EnumType.STRING) var providerType: OAuthProviderType? = null,
    @Column(unique = true)
    val email: String? = null,
    var name: String? = null,
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<RoleType> = mutableSetOf(),


    ) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        val authorities = roles.map { SimpleGrantedAuthority("ROLE_${it.name}") }.toMutableList()
        roles.forEach { role ->
            val permissionsForRoles = RoleToPermission.mapping[role] ?: mutableSetOf()
            permissionsForRoles.forEach { permission ->
                authorities.add(SimpleGrantedAuthority(permission.value))
            }

        }
        return authorities
    }

    override fun getPassword(): String? {
        return password
    }

    override fun getUsername(): String {
        return username
    }
}