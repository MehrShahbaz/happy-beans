package happybeans.model

import happybeans.enums.PaymentOption
import happybeans.enums.PaymentStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
class Payment(
    @Column(name = "payment_id", nullable = false)
    val paymentId: String,
    @Column(name = "amount", nullable = false)
    val amount: Double,
    @Column(name = "order_id", nullable = false)
    val orderId: Long,
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    var status: PaymentStatus = PaymentStatus.IN_PROGRESS,
    @Column(name = "payment_option", nullable = false)
    @Enumerated(EnumType.STRING)
    var paymentOption: PaymentOption = PaymentOption.STRIPE,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
