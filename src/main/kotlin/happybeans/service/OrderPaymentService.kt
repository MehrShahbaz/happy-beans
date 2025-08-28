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
        logger.info("Checkout for member: ${member.id}")
        val order = memberOrderService.checkoutCart(member)
        logger.info("Session for member: ${member.id}")
        val session = stripePaymentService.createSession(order)
        logger.info("Set session id for order: ${order.id}")
        memberOrderService.setPaymentId(order, session.id)
        logger.info("Create payment for order: ${order.id}")
        paymentService.createPayment(session.id, order)
        return session.url
    }

    fun handleBuyDish(
        member: User,
        dishOptionId: Long,
    ): String {
        logger.info("Buy dish for member: ${member.id} dish option id: $dishOptionId")
        val order = memberOrderService.buyProduct(member, dishOptionId)
        logger.info("Buy dish Session for member: ${member.id}")
        val session = stripePaymentService.createSession(order)
        logger.info("Create payment for dish order: ${order.id}")
        paymentService.createPayment(session.id, order)
        return session.url
    }

    fun handlePaymentSuccess(event: StripeEventDto?) {
        if (event == null) {
            logger.error { "event not found" }
            return
        }
        logger.info("Success: Payment success for event: ${event.id}")
        logger.info("Success: Find order by id: ${getOrderId(event)}")
        val order = memberOrderService.getOrder(getOrderId(event))
        logger.info("Success: Find payment by order id: ${getOrderId(event)}")
        val payment = paymentService.getPaymentByOrderId(order.id)

        logger.info("Success: Payment success for payment: ${payment.id}")
        paymentService.updateStatus(payment, PaymentStatus.COMPLETED)
        logger.info("Success: Payment success for order: ${order.id}")
        memberOrderService.updateStatus(order, OrderStatus.COMPLETED)

        logger.info("Success: Clear cart for member: ${order.userId}")
        cartProductService.clearPaymentSuccess(order.userId)

        logger.info("Success: Sending confirmation email for order: ${order.id} for email: ${order.userEmail}")
        emailDispatchService.sendOrderConfirmationEmail(order)
    }

    fun handlePaymentFailure(event: StripeEventDto?) {
        if (event == null) {
            logger.error { "event not found" }
            return
        }
        logger.info("Failure: Payment failed for event: ${event.id}")
        logger.info("Find order by id: ${getOrderId(event)}")
        val order = memberOrderService.getOrder(getOrderId(event))
        logger.info("Find payment by order id: ${getOrderId(event)}")
        val payment = paymentService.getPaymentByOrderId(order.id)

        logger.info("Payment fail for payment: ${payment.id}")
        paymentService.updateStatus(payment, PaymentStatus.FAILED)
        logger.info("Payment fail for order: ${order.id}")
        memberOrderService.updateStatus(order, OrderStatus.REJECTED)

        logger.info("Sending fail email for order: ${order.id} for email: ${order.userEmail}")
        emailDispatchService.sendOrderFailEmail(order)
    }

    private fun getOrderId(event: StripeEventDto): Long {
        val pi = event.data.`object`
        return pi.metadata["orderId"]?.toLong() ?: 0L
    }
}
