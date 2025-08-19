package happybeans.controller.member

import happybeans.dto.order.OrderListResponse
import happybeans.dto.order.OrderResponse
import happybeans.dto.response.MessageResponse
import happybeans.model.User
import happybeans.service.MemberOrderService
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
    ): ResponseEntity<MessageResponse> {
        val response = orderService.createCheckoutCartIntent(user)
        return ResponseEntity.ok(MessageResponse("Cart Checkout"))
    }

    @PostMapping("/confirm-checkout/{orderId}")
    fun confirmCheckout(
        @LoginMember user: User,
        @PathVariable orderId: Long,
    ): ResponseEntity<MessageResponse> {
        val response = orderService.createCheckoutCartIntent(user)
        return ResponseEntity.ok(MessageResponse("confirm-checkout/$orderId"))
    }

    @PostMapping("/buy-dish/{dishOptionId}")
    fun buyProduct(
        @LoginMember user: User,
        @PathVariable dishOptionId: Long,
    ): ResponseEntity<MessageResponse> {
        val response = orderService.buyProduct(user, dishOptionId)
        return ResponseEntity.ok(MessageResponse("buy-product/$dishOptionId"))
    }
}
