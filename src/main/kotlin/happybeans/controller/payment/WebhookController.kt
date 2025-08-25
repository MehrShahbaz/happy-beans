package happybeans.controller.payment

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.stripe.net.Webhook
import happybeans.dto.stripe.StripeEventDto
import happybeans.service.OrderPaymentService
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
    private val orderPaymentService: OrderPaymentService,
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

        val mapper =
            jacksonObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        val stripeEvent: StripeEventDto? =
            try {
                mapper.readValue<StripeEventDto>(payload)
            } catch (e: Exception) {
                println("Error parsing Stripe webhook payload: ${e.message}")
                null
            }

        when (event.type) {
            "payment_intent.succeeded" -> {
                orderPaymentService.handlePaymentSuccess(stripeEvent)
                println("✅ Payment succeeded")
            }
            "payment_intent.payment_failed" -> {
                orderPaymentService.handlePaymentFailure(stripeEvent)
                println("❌ Payment failed")
            }
        }

        return "ok"
    }
}
