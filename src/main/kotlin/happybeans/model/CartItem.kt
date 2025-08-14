package happybeans.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import jakarta.persistence.UniqueConstraint
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime


@Entity
@Table(
    name = "cart_items",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "dish_option_id"])],
)
open class CartItem protected constructor()
{
    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User
    @OneToOne(cascade = [(CascadeType.ALL)], fetch = FetchType.EAGER)
    @JoinColumn(name = "dish_option_id", nullable = false)
    lateinit var dishOption: DishOption
    @Column(name = "quantity", nullable = false)
    var quantity: Int = 1
    @CreationTimestamp
    var createdAt: LocalDateTime? = null
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    constructor(user: User, dishOption: DishOption) : this() {
        this.user = user
        this.dishOption = dishOption
    }

    fun incrementQuantity(quantity: Int = 1) {
        this.quantity += quantity
    }

    fun decrementQuantity(quantity: Int = 1) {
        this.quantity -= quantity
    }

    operator fun plusAssign(value: Int) {
        require(value > 0) { "value must be positive." }
        this.quantity += value
    }

    operator fun minusAssign(value: Int) {
        require(value > 0) { "value must be positive." }
        require(quantity >= value) { "quantity must be gater than value." }
        this.quantity -= value
    }
}
