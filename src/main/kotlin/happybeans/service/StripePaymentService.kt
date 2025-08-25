package happybeans.service

import com.stripe.model.checkout.Session
import com.stripe.param.checkout.SessionCreateParams
import happybeans.model.Order
import happybeans.model.OrderProduct
import org.springframework.stereotype.Service

@Service
class StripePaymentService() {
    fun createSession(order: Order): Session {
        val params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:80/payment-success.html")
                .setCancelUrl("http://localhost:80/payment-failed.html")
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
