package happybeans.dto.review

import java.time.LocalDateTime


class ReviewCreateResponse (
    val id: Long,
    val userId: Long,
    val rating: Int,
    val message: String?,
    val dishId: Long?,
    val restaurantId: Long?,
    val createdAt: LocalDateTime
)