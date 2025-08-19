package happybeans.dto.dish

import jakarta.validation.constraints.NotNull

data class DishOptionCreateRequest(
    // TODO: check when to add `@field:valid`
    @field:NotNull
    val name: String,
    val description: String?,
    @field:NotNull
    val price: Double,
    @field:NotNull
    val image: String,
    @field:NotNull
    val ingredients: Set<String>,
    val available: Boolean = true,
    @field:NotNull
    val rating: Double,
    @field:NotNull
    val prepTimeMinutes: Int,
)
