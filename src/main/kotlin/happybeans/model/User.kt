package happybeans.model

import happybeans.enums.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class User(
    @Column(name = "email", nullable = false)
    var email: String,
    @Column(name = "password", nullable = false)
    var password: String,
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val restaurants: List<String> = listOf(),
    val dislikes: List<String> = listOf(),
    val preferences: List<String> = listOf(),
)
