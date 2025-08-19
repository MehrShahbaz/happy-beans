package happybeans.dto.dish

data class DishResponse(
    val id: Long?,
    val name: String,
    val description: String,
    val image: String,
    val restaurantId: Long?,
    val dishOptions: Set<DishOptionResponse> = emptySet(),
)
