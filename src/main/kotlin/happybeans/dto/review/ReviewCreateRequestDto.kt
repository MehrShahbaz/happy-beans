package happybeans.dto.review

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

class ReviewCreateRequestDto(
    @field:NotNull(message = "User ID is required")
    val userId: Long,
    @field:NotNull(message = "Rating is required")
    @field:Min(value = 1, message = "Rating must be at least 1")
    @field:Max(value = 5, message = "Rating must be at most 5")
    val rating: Int,
    @field:Size(max = 500, message = "Message cannot exceed 500 characters")
    val message: String?,
    val dishId: Long?,
    val restaurantId: Long?,
) {
    init {
        require(dishId != null || restaurantId != null) { "Review should be associated with wither dish or restaurant" }
    }
}