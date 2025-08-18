package happybeans.service

import com.stripe.model.PaymentIntent
import com.stripe.param.PaymentIntentCreateParams
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
}
