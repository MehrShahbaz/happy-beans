package happybeans.service

import happybeans.enums.PaymentStatus
import happybeans.model.Order
import happybeans.model.Payment
import happybeans.repository.PaymentRepository
import happybeans.utils.exception.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PaymentService(private val paymentRepository: PaymentRepository) {
    private val logger = KotlinLogging.logger {}

    fun getPaymentByOrderId(orderId: Long): Payment {
        return paymentRepository.findByOrderId(orderId).orElseThrow {
            logger.error { "Payment not found for orderId=$orderId" }
            EntityNotFoundException("Payment with id $orderId not found")
        }
    }

    @Transactional
    fun updateStatus(
        payment: Payment,
        status: PaymentStatus,
    ) {
        logger.info { "Updating status for payment=${payment.id} from status=${payment.status} to status=$status" }
        payment.status = status
        paymentRepository.save(payment)
    }

    fun createPayment(
        paymentId: String,
        order: Order,
    ): Payment {
        logger.info { "Creating payment for order: ${order.id}" }
        return paymentRepository.save(
            Payment(
                paymentId,
                order.totalAmount,
                order.id,
            ),
        )
    }
}
