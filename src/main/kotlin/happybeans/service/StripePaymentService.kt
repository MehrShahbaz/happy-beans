package happybeans.service

import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import happybeans.model.Order
import happybeans.model.OrderProduct
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class StripePaymentService(
    @Value("\${app.url}")
    private val url: String,
) {
    private val logger = KotlinLogging.logger {}

    fun createSession(order: Order): Session {
        logger.info { "Creating session for order: ${order.id}" }
        val params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("$url/payment-success.html")
                .setCancelUrl("$url/payment-failed.html")
                .putMetadata("orderId", "${order.id}")
                .setPaymentIntentData(
                    SessionCreateParams.PaymentIntentData.builder()
                        .putMetadata("orderId", "${order.id}").build(),
                )
                .addAllLineItem(order.orderProducts.map { it.toLineItem() })
                .build()

        return Session.create(params)
    }

    private fun OrderProduct.toLineItem(): SessionCreateParams.LineItem {
        return SessionCreateParams.LineItem.builder()
            .setQuantity(quantity.toLong())
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("eur")
                    .setUnitAmount(convertPrice(price))
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(dishOptionName)
                            .build(),
                    )
                    .build(),
            )
            .build()
    }

    private fun convertPrice(price: Double): Long = (price * 100).toLong()
}
