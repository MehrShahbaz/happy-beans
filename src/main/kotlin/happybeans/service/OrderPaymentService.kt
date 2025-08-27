package happybeans.service

import happybeans.dto.stripe.StripeEventDto
import happybeans.enums.OrderStatus
import happybeans.enums.PaymentStatus
import happybeans.model.User
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class OrderPaymentService(
    private val memberOrderService: MemberOrderService,
    private val stripePaymentService: StripePaymentService,
    private val paymentService: PaymentService,
    private val emailDispatchService: EmailDispatchService,
    private val cartProductService: CartProductService,
) {
    private val logger = KotlinLogging.logger {}

    fun handleCartCheckout(member: User): String {
        val order = memberOrderService.checkoutCart(member)
        val session = stripePaymentService.createSession(order)
        paymentService.createPayment(session.id, order)
        return session.url
    }

    fun handleBuyDish(
        member: User,
        dishOptionId: Long,
    ): String {
        val order = memberOrderService.buyProduct(member, dishOptionId)
        val session = stripePaymentService.createSession(order)
        paymentService.createPayment(session.id, order)
        return session.url
    }

    fun handlePaymentSuccess(event: StripeEventDto?) {
        if (event == null) {
            logger.error { "event not found" }
            return
        }
        val order = memberOrderService.getOrder(getOrderId(event))
        val payment = paymentService.getPaymentByOrderId(order.id)

        paymentService.updateStatus(payment, PaymentStatus.COMPLETED)
        memberOrderService.updateStatus(order, OrderStatus.COMPLETED)

        cartProductService.clearPaymentSuccess(order.userId)

        emailDispatchService.sendOrderConfirmationEmail(order)
    }

    fun handlePaymentFailure(event: StripeEventDto?) {
        if (event == null) {
            logger.error { "event not found" }
            return
        }
        val order = memberOrderService.getOrder(getOrderId(event))
        val payment = paymentService.getPaymentByOrderId(order.id)

        paymentService.updateStatus(payment, PaymentStatus.FAILED)
        memberOrderService.updateStatus(order, OrderStatus.REJECTED)

        emailDispatchService.sendOrderFailEmail(order)
    }

    private fun getOrderId(event: StripeEventDto): Long {
        val pi = event.data.`object`
        return pi.metadata["orderId"]?.toLong() ?: 0L
    }
}
