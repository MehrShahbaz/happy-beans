package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
class Review(
    @Column(name = "userId", nullable = false)
    val userId: Long,
    @Column(name = "rating", nullable = false)
    val rating: Int,
    @Column(name = "message", nullable = true)
    var message: String? = null,
    @Column(name = "dishId", nullable = true)
    var dishId: Long? = null,
    @Column(name = "restaurantId", nullable = true)
    var restaurantId: Long? = null,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)