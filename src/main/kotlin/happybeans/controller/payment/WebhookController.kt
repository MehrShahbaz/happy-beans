package happybeans.controller.payment

import com.stripe.net.Webhook
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment/webhook")
class WebhookController(
    @Value("\${stripe.webhookKey}")
    private val endpointSecret: String,
) {
    @PostMapping
    fun handleWebhook(
        @RequestBody payload: String,
        request: HttpServletRequest,
    ): String {
        val sigHeader = request.getHeader("Stripe-Signature") ?: return "Missing signature"

        val event =
            try {
                Webhook.constructEvent(payload, sigHeader, endpointSecret)
            } catch (e: Exception) {
                return "Invalid signature"
            }

        when (event.type) {
            "payment_intent.succeeded" -> println("✅ Payment succeeded")
            "payment_intent.payment_failed" -> println("❌ Payment failed")
        }

        return "ok"
    }
}
