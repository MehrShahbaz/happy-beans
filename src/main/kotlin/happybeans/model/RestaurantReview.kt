package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class RestaurantReview(
    @Column(name = "user_id", nullable = false)
    var userId: Long,
    @Column(name = "user_name", nullable = false)
    var userName: String,
    @Column(name = "rating", nullable = false)
    var rating: Double,
    @Column(name = "message", nullable = true)
    var message: String,
    @Column(name = "restaurant_id")
    var restaurantId: Long,
    @Column(name = "restaurant_name")
    var restaurantName: String,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
