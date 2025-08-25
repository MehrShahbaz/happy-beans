package happybeans.controller.member

import happybeans.dto.order.OrderListResponse
import happybeans.dto.order.OrderResponse
import happybeans.model.User
import happybeans.service.MemberOrderService
import happybeans.service.OrderPaymentService
import happybeans.utils.annotations.LoginMember
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member/orders")
class MemberOrderController(
    private val orderService: MemberOrderService,
    private val orderPaymentService: OrderPaymentService,
) {
    @GetMapping("")
    fun getAllOrders(
        @LoginMember user: User,
    ): ResponseEntity<OrderListResponse> {
        val response = OrderListResponse(orderService.getAllUserOrders(user.id))
        return ResponseEntity.ok(response)
    }

    @GetMapping("/{orderId}")
    fun getOrderById(
        @PathVariable orderId: Long,
    ): ResponseEntity<OrderResponse> {
        return ResponseEntity.ok(orderService.getOrderById(orderId))
    }

    @PostMapping("/cart-checkout")
    fun createCheckoutCartIntent(
        @LoginMember user: User,
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("paymentUrl" to orderPaymentService.handleCartCheckout(user)))
    }

    @PostMapping("/buy-dish/{dishOptionId}")
    fun buyProduct(
        @LoginMember user: User,
        @PathVariable dishOptionId: Long,
    ): ResponseEntity<Map<String, String>> {
        return ResponseEntity.ok(mapOf("paymentUrl" to orderPaymentService.handleBuyDish(user, dishOptionId)))
    }
}
