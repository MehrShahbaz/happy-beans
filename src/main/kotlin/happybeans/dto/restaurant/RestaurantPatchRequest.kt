package happybeans.dto.restaurant

import jakarta.validation.Valid

class RestaurantPatchRequest(
    val name: String? = null,
    val description: String? = null,
    val image: String? = null,
    val addressUrl: String? = null,
    @field:Valid
    var workingDateHours: List<WorkingDateHourRequest>? = null,
)
