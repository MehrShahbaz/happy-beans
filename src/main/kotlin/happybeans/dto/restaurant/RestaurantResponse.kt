package happybeans.dto.restaurant

import happybeans.model.Restaurant
import java.time.LocalDateTime

class RestaurantResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val addressUrl: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
) {
    constructor(restaurant: Restaurant) : this(
        restaurant.id,
        restaurant.name,
        restaurant.description,
        restaurant.image,
        restaurant.addressUrl,
        restaurant.createdAt,
        restaurant.updatedAt,
    )
}
