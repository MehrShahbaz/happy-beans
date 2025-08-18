package happybeans.dto.dish

import jakarta.validation.constraints.NotNull

data class DishCreateRequest(
    @field:NotNull(message = "Name is required")
    val name: String,
    @field:NotNull(message = "Description is required")
    val description: String,
    @field:NotNull(message = "Image URL is required")
    val image: String,
    @field:NotNull(message = "Working date and hours is required")
    val dishOptionRequests: MutableSet<DishOptionCreateRequest>,
)
