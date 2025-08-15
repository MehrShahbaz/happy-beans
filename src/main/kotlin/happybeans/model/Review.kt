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
class Review(
    @Column(name="user_id", nullable = false)
    var userId: Long,
    @Column(name="user_name", nullable = false)
    var userName: Long,
    @Column(name = "rating", nullable = false)
    var rating: Double,
    @Column(name = "message", nullable = true)
    var message: String,
    @Column(name = "dish_option_id")
    var dishOptionId: Long? = null,
    @Column(name = "dish_option_name")
    var dishOptionName: Long? = null,
    @Column(name = "dish_option_price")
    var dishOptionPrice: Double? = null,
    @Column(name = "restaurant_id")
    var restaurantId: Long? = null,
    @Column(name = "restaurant_name")
    var restaurantName: String? = null,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    // TODO Add validations if dish than this and if res than this
    // TODO Only message can be updated
    // TODO Rating 1-5
    // TODO Split in restaurant and dish review
}