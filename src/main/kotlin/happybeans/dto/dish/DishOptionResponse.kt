package happybeans.dto.dish

data class DishOptionResponse(
    val id: Long?,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val ingredients: Set<String> = emptySet(),
    val available: Boolean,
    val prepTimeMinutes: Int,
    val rating: Double,
    val dishId: Long?,
)
