package happybeans.dto.restaurant

import happybeans.model.Dish
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class RestaurantCreateRequest(
    @field:NotNull(message = "Name is required")
    val name: String,
    @field:NotNull(message = "Description is required")
    val description: String,
    @field:NotNull(message = "Image URL is required")
    val image: String,
    @field:NotNull(message = "Address URL is required")
    val addressUrl: String,
    @field:Valid
    @field:NotNull(message = "Working Dat Hours is required")
    var workingDateHours: List<WorkingDateHourRequest> = listOf(),
    @field:Valid
    @field:NotNull(message = "Dishes are required")
    val dishes: MutableList<Dish> = mutableListOf(),
)
