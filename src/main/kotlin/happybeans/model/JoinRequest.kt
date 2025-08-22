package happybeans.model

import happybeans.enums.JoinRequestStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class JoinRequest(
    @Column(name = "email", unique = true)
    val email: String,
    @Column(name = "first_name", unique = true)
    val firstName: String,
    @Column(name = "last_name", unique = true)
    val lastName: String,
    @Column(name = "message", unique = true)
    val message: String,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: JoinRequestStatus = JoinRequestStatus.PENDING,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
