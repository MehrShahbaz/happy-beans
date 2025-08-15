package happybeans.service

import happybeans.dto.order.OrderIntentResponse
import happybeans.dto.order.OrderProductResponse
import happybeans.dto.order.OrderResponse
import happybeans.enums.OrderStatus
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

    fun createCheckoutCartIntent(member: User): OrderIntentResponse {
        // TODO cart items
        // TODO create payment Intent
        // TODO Create Payment entity
        val order = createCartOrder(member)
        return OrderIntentResponse("paymentIntentId", order.id)
    }

    fun confirmCheckout(
        member: User,
        orderId: Long,
    ) {
        val order = findOrder(orderId)
        // TODO Check cart products
        try {
            // TODO confirm payment
            // TODO empty cart
            order.changeStatus(OrderStatus.COMPLETED)
        } catch (e: RuntimeException) {
            // TODO Reject Payment
            order.changeStatus(OrderStatus.REJECTED)
            throw IllegalArgumentException()
        }
    }

    fun buyProduct(
        member: User,
        dishId: Long,
    ) {
        // TODO find dish
        val order = createProductOrder(member)
        // TODO Buy Product via stripe
    }

    private fun createProductOrder(member: User): Order {
        return Order(
            // TODO add product here
            listOf(),
            member.id,
            member.email,
            "paymentId",
            0.0,
        )
    }

    private fun createCartOrder(member: User): Order {
        return Order(
            listOf(),
            member.id,
            member.email,
            "paymentId",
            0.0,
        )
    }

    private fun findOrder(orderId: Long): Order {
        return orderRepository.findById(orderId).orElseThrow {
            throw EntityNotFoundException("Order with id $orderId not found")
        }
    }

    private fun Order.toOrderResponse(): OrderResponse {
        return OrderResponse(
            id,
            createdAt!!,
            status,
            totalAmount,
            paymentId,
            orderProducts.map { it.toResponse() },
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
