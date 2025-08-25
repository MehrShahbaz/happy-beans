package happybeans.service

import happybeans.enums.PaymentStatus
import happybeans.model.Order
import happybeans.model.Payment
import happybeans.repository.PaymentRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(private val paymentRepository: PaymentRepository) {
    fun getPaymentByOrderId(orderId: Long): Payment {
        return paymentRepository.findByOrderId(orderId).orElseThrow {
            EntityNotFoundException("Payment with id $orderId not found")
        }
    }

    @Transactional
    fun updateStatus(
        payment: Payment,
        status: PaymentStatus,
    ) {
        payment.status = status
        paymentRepository.save(payment)
    }

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
