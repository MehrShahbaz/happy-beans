package happybeans.dto.review

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

class ReviewCreateRequestDto(
    @field:NotNull(message = "Rating is required")
    @field:Min(value = 1, message = "Rating must be at least 1")
    @field:Max(value = 5, message = "Rating must be at most 5")
    val rating: Double,
    @field:Size(max = 500, message = "Message cannot exceed 500 characters")
    val message: String = "",
    @field:NotNull(message = "Id is required")
    val entityId: Long,
)

class ReviewUpdateRequestDto(
    @field:Size(max = 500, message = "Message cannot exceed 500 characters")
    val message: String,
)

//class ReviewCreateResponse(
//    val id: Long,
//    val rating: Double,
//    val message: String?,
//    val entityId: Long,
//    val createdAt: LocalDateTime,
//)

//class ReviewUpdateResponse(
//    val id: Long,
//    val message: String,
//    val updatedAt: LocalDateTime,
//)

class DishReviewDto(
    val id: Long,
    val userName: String,
    val rating: Double,
    val message: String,
    val dishOptionId: Long,
    val dishOptionName: String? = null,
    val dishOptionPrice: Double?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)

class RestaurantReviewDto(
    val id: Long,
    val userName: String,
    val rating: Double,
    val message: String,
    val restaurantId: Long,
    val restaurantName: String? = null,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
)
