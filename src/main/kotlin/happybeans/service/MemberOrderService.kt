package happybeans.service

import happybeans.dto.order.OrderProductResponse
import happybeans.dto.order.OrderResponse
import happybeans.enums.OrderStatus
import happybeans.model.CartProduct
import happybeans.model.DishOption
import happybeans.model.Order
import happybeans.model.OrderProduct
import happybeans.model.User
import happybeans.repository.CartProductRepository
import happybeans.repository.DishOptionRepository
import happybeans.repository.OrderRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemberOrderService(
    private val orderRepository: OrderRepository,
    private val cartProductRepository: CartProductRepository,
    private val dishOptionRepository: DishOptionRepository,
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

    @Transactional(readOnly = true)
    fun getOrder(orderId: Long): Order {
        return orderRepository.findById(orderId).orElseThrow {
            EntityNotFoundException("Order with id $orderId not found")
        }
    }

    @Transactional
    fun updateStatus(
        order: Order,
        status: OrderStatus,
    ) {
        order.status = status
        orderRepository.save(order)
    }

    @Transactional
    fun checkoutCart(member: User): Order {
        val cartProducts = cartProductRepository.findAllByUserId(member.id)
        if (cartProducts.isEmpty()) {
            throw EntityNotFoundException("No cart-product found")
        }

        val order = createOrder(member)
        order.orderProducts.addAll(cartProducts.map { it.toOrderEntity() })
        order.totalAmount = order.orderProducts.sumOf { it.price }
        orderRepository.save(order)
        return order
    }

    @Transactional
    fun buyProduct(
        member: User,
        dishOptionId: Long,
    ): Order {
        val dishOption =
            dishOptionRepository.findById(dishOptionId).orElseThrow {
                EntityNotFoundException("dish-option $dishOptionId not found")
            }
        val order = createOrder(member)
        order.orderProducts.add(dishOption.toOrderEntity())
        order.totalAmount = dishOption.price
        orderRepository.save(order)
        return order
    }

    private fun createOrder(member: User): Order {
        return Order(
            userId = member.id,
            userEmail = member.email,
            paymentId = "paymentId",
        )
    }

    private fun CartProduct.toOrderEntity(): OrderProduct {
        return OrderProduct(
            dishOption.id,
            dishOption.name,
            dishOption.price,
            quantity,
        )
    }

    private fun DishOption.toOrderEntity(): OrderProduct {
        return OrderProduct(
            id,
            name,
            price,
            1,
        )
    }

    private fun Order.toOrderResponse(): OrderResponse {
        return OrderResponse(
            id,
            createdAt!!,
            status,
            totalAmount,
            paymentId!!,
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
