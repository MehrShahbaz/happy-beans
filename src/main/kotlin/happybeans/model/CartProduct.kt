package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "cart_products",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "dish_option_id"])],
)
class CartProduct(
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    val dish: Dish,
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_option_id", nullable = false)
    val dishOption: DishOption,
    @Column(name = "quantity", nullable = false)
    var quantity: Int = 1,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    init {
        require(quantity > 0) { "Quantity must be greater than zero" }
    }

    @PrePersist
    @PreUpdate
    fun validate() {
        require(quantity > 0) { "Quantity must be greater than zero (JPA lifecycle)" }
    }
}
