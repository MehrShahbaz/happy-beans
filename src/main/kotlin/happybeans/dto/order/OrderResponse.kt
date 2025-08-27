package happybeans.dto.order

import happybeans.enums.OrderStatus
import java.time.LocalDateTime

class OrderResponse(
    val orderId: Long,
    val createdAt: LocalDateTime,
    val status: OrderStatus,
    val totalAmount: Double,
    val paymentId: String,
    val products: List<OrderProductResponse>,
)
