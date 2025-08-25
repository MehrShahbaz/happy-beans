package happybeans.dto.restaurant

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

class RestaurantPatchRequest(
    @field:NotNull(message = "Name is required")
    val name: String? = null,
    @field:NotNull(message = "Description is required")
    val description: String? = null,
    @field:NotNull(message = "Image URL is required")
    val image: String? = null,
    @field:NotNull(message = "Address URL is required")
    val addressUrl: String? = null,
    @field:Valid
    @field:NotNull(message = "Working Dat Hours is required")
    var workingDateHours: List<WorkingDateHourRequest>? = null,
)
