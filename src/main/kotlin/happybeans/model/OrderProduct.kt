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
class OrderProduct(
    @Column(name = "dish_option_id")
    var dishOptionId: Long,
    @Column(name = "dish_option_name")
    var dishOptionName: String,
    @Column(name = "dish_id")
    var dishId: Long,
    @Column(name = "price", nullable = false)
    var price: Double,
    @Column(name = "quantity", nullable = false)
    var quantity: Int,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
