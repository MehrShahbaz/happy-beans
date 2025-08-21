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
class DishReview(
    @Column(name = "user_id", nullable = false)
    var userId: Long,
    @Column(name = "user_name", nullable = false)
    var userName: String,
    @Column(name = "rating", nullable = false)
    var rating: Double,
    @Column(name = "message", nullable = true)
    var message: String,
    @Column(name = "dish_option_id")
    var dishOptionId: Long,
    @Column(name = "dish_option_name")
    var dishOptionName: String? = null,
    @Column(name = "dish_option_price")
    var dishOptionPrice: Double? = null,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
