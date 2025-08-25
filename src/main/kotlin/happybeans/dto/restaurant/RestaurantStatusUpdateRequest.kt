package happybeans.dto.restaurant

import happybeans.enums.RestaurantStatus

data class RestaurantStatusUpdateRequest(
    val status: RestaurantStatus,
)
