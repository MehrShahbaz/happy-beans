package happybeans.controller.member

import happybeans.dto.order.OrderListResponse
import happybeans.dto.order.OrderResponse
import happybeans.model.User
import happybeans.service.MemberOrderService
import happybeans.service.OrderPaymentService
import happybeans.utils.annotations.LoginMember
import mu.KotlinLogging
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
    private val logger = KotlinLogging.logger {}

    @GetMapping("")
    fun getAllOrders(
        @LoginMember user: User,
    ): ResponseEntity<OrderListResponse> {
        logger.info("GET all orders for user: ${user.id}")
        return ResponseEntity.ok(orderService.getAllUserOrders(user.id))
    }

    @GetMapping("/{orderId}")
    fun getOrderById(
        @PathVariable orderId: Long,
    ): ResponseEntity<OrderResponse> {
        logger.info("GET order with id: $orderId")
        return ResponseEntity.ok(orderService.getOrderById(orderId))
    }

    @PostMapping("/cart-checkout")
    fun createCheckoutCartIntent(
        @LoginMember user: User,
    ): ResponseEntity<Map<String, String>> {
        logger.info("POST cart checkout for user: ${user.id}")
        return ResponseEntity.ok(mapOf("paymentUrl" to orderPaymentService.handleCartCheckout(user)))
    }

    @PostMapping("/buy-dish/{dishOptionId}")
    fun buyProduct(
        @LoginMember user: User,
        @PathVariable dishOptionId: Long,
    ): ResponseEntity<Map<String, String>> {
        logger.info("POST Buy dish for user: ${user.id} dishOptionId: $dishOptionId")
        return ResponseEntity.ok(mapOf("paymentUrl" to orderPaymentService.handleBuyDish(user, dishOptionId)))
    }
}
