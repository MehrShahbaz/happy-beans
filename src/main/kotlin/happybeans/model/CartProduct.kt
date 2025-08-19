package happybeans.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
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
open class CartProduct protected constructor() {
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_option_id", nullable = false)
    lateinit var dishOption: DishOption

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_id", nullable = false)
    lateinit var dish: Dish

    @Column(name = "quantity", nullable = false)
    var quantity: Int = 1
        set(value) {
            require(value > 0) { "New value must be greater than zero" }
            field = value
        }

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    constructor(user: User, dish: Dish, dishOption: DishOption) : this() {
        this.user = user
        this.dishOption = dishOption
        this.dish = dish
    }
}
