package happybeans.dto.restaurant

import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.toResponse
import happybeans.model.Restaurant
import happybeans.model.WorkingDateHour

class RestaurantResponseDto(
    val id: Long,
    val ownerId: Long,
    val name: String,
    val description: String,
    val image: String,
    val addressUrl: String,
    val workingDateHours: List<WorkingDateHour>,
    val dishes: List<DishResponse>,
)

fun Restaurant.toResponse(): RestaurantResponseDto {
    return RestaurantResponseDto(
        id,
        user.id,
        name,
        description,
        image,
        addressUrl,
        workingDateHours,
        dishes.map { it.toResponse() },
    )
}
