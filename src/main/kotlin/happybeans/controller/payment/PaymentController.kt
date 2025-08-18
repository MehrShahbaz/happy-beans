package happybeans.controller.payment

import happybeans.dto.response.MessageResponse
import happybeans.service.PaymentService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payment-intent")
class PaymentController(private val paymentService: PaymentService) {
    @GetMapping("")
    fun getPaymentIntent(): ResponseEntity<MessageResponse> {
        val data = paymentService.createPayment(1000)
        return ResponseEntity.ok(MessageResponse(data.id))
    }
}
