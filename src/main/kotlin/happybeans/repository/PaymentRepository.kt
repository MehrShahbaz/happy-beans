package happybeans.repository

import happybeans.model.Payment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface PaymentRepository : JpaRepository<Payment, Long> {
    fun findByOrderId(orderId: Long): Optional<Payment>
}
