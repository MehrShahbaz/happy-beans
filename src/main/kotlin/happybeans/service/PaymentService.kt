package happybeans.service

import com.stripe.model.PaymentIntent
import com.stripe.model.checkout.Session
import com.stripe.param.PaymentIntentCreateParams
import com.stripe.param.checkout.SessionCreateParams
import org.springframework.stereotype.Service

@Service
class PaymentService() {
    fun createPayment(
        amount: Long,
        currency: String = "eur",
    ): PaymentIntent {
        return try {
            val params =
                PaymentIntentCreateParams
                    .builder()
                    .setAmount(amount)
                    .setCurrency(currency)
                    .build()

            PaymentIntent.create(params)
        } catch (e: Exception) {
            throw IllegalStateException("Error creating payment", e)
        }
    }

    fun createSession(): String {
        val params =
            SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:80/payment-success.html")
                .setCancelUrl("http://localhost:80/payment-failed.html")
                .addAllLineItem(listOf(createLineItem1()))
                .build()

        return Session.create(params).url
    }

    private fun createLineItem1(): SessionCreateParams.LineItem {
        return SessionCreateParams.LineItem.builder()
            .setQuantity(4L)
            .setPriceData(
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(2000) // $20.00
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Chicken Sandwich")
                            .setDescription("Best Chicken Sandwich")
                            .build(),
                    )
                    .build(),
            )
            .build()
    }
}
