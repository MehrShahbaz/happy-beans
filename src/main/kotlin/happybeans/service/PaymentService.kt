package happybeans.service

import happybeans.model.Order
import happybeans.model.Payment
import happybeans.repository.PaymentRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun createPayment(
        paymentId: String,
        order: Order,
    ): Payment {
        return paymentRepository.save(
            Payment(
                paymentId,
                order.totalAmount,
                order.id,
            ),
        )
    }
}
