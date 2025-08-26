package happybeans.dto.dish

import jakarta.validation.constraints.NotNull

data class DishOptionCreateRequest(
    @field:NotNull
    val name: String,
    @field:NotNull
    val description: String?,
    @field:NotNull
    val price: Double,
    @field:NotNull
    val image: String,
    val available: Boolean = true,
    @field:NotNull
    val prepTimeMinutes: Int,
)
