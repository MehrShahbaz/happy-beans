package happybeans.service

import happybeans.model.User
import org.springframework.stereotype.Service

@Service
class OrderPaymentService(
    private val memberOrderService: MemberOrderService,
    private val stripePaymentService: StripePaymentService,
    private val paymentService: PaymentService,
) {
    fun handleCartCheckout(member: User): String {
        val order = memberOrderService.checkoutCart(member)
        val session = stripePaymentService.createSession(order)
        paymentService.createPayment(session.paymentIntent, order)
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
}
