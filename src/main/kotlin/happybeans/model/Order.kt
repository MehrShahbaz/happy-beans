package happybeans.model

import happybeans.enums.CreationSource
import happybeans.enums.OrderStatus
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "orders_id", nullable = false)
    val orderProducts: MutableList<OrderProduct> = mutableListOf(),
    @Column(name = "user_id", nullable = false)
    var userId: Long,
    @Column(name = "user_email", nullable = false)
    var userEmail: String,
    @Column(name = "payment_id", nullable = false)
    val paymentId: String? = null,
    @Column(name = "total_amount", nullable = false)
    var totalAmount: Double = 0.0,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: OrderStatus = OrderStatus.PENDING,
    @Column(name = "creation_source", nullable = false)
    @Enumerated(EnumType.STRING)
    var creationSource: CreationSource = CreationSource.USER,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    fun changeStatus(status: OrderStatus) {
        this.status = status
    }
}
