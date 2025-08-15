package happybeans.service

import happybeans.dto.order.OrderProductResponse
import happybeans.dto.order.OrderResponse
import happybeans.model.Order
import happybeans.model.OrderProduct
import happybeans.model.User
import happybeans.repository.OrderRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberOrderService(
    private val orderRepository: OrderRepository,
) {
    @Transactional(readOnly = true)
    fun getAllUserOrders(userId: Long): List<OrderResponse> {
        return orderRepository.findAllByUserId(userId).map { it.toOrderResponse() }
    }

    @Transactional(readOnly = true)
    fun getOrderById(orderId: Long): OrderResponse {
        return orderRepository.findById(orderId).orElseThrow {
            EntityNotFoundException("Order with id $orderId not found")
        }.toOrderResponse()
    }

    fun createCheckoutCartIntent(member: User) {}

    fun confirmCheckout(
        member: User,
        orderId: Long,
    ) {}

    fun buyProduct(
        member: User,
        productId: Long,
    ) {}

    private fun Order.toOrderResponse(): OrderResponse {
        return OrderResponse(
            id,
            createdAt!!,
            status,
            totalAmount,
            paymentId,
            listOf(),
        )
    }

    private fun OrderProduct.toResponse(): OrderProductResponse {
        return OrderProductResponse(
            price,
            quantity,
            null,
        )
    }
}
