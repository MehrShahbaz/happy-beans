package happybeans.dto.dish

import happybeans.model.DishOption

data class DishOptionResponse(
    val id: Long?,
    val name: String,
    val description: String,
    val price: Double,
    val image: String,
    val available: Boolean,
    val prepTimeMinutes: Int,
    val dishId: Long?,
)

fun DishOption.toResponse(): DishOptionResponse {
    return DishOptionResponse(
        id = this.id,
        name = this.name,
        description = this.description ?: "",
        price = this.price,
        image = this.image,
        available = this.available,
        prepTimeMinutes = this.prepTimeMinutes,
        dishId = this.dish.id,
    )
}
