package happybeans.dto.dish

import happybeans.model.Dish

data class DishResponse(
    val id: Long?,
    val name: String,
    val description: String,
    val image: String,
    val restaurantId: Long?,
    val dishOptions: Set<DishOptionResponse> = emptySet(),
)

fun Dish.toResponse(restaurantId: Long? = null): DishResponse {
    return DishResponse(
        id = this.id,
        name = this.name,
        description = this.description,
        image = this.image,
        restaurantId = restaurantId,
        dishOptions = this.dishOptions.map { it.toResponse() }.toSet(),
    )
}
