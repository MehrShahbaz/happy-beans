package happybeans.dto.dish

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.Size

data class DishOptionPatchRequest(
    @field:Size(max = 100, message = "Option name must not exceed 100 characters")
    val name: String? = null,
    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,
    @field:DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    @field:DecimalMax(value = "999.99", message = "Price must not exceed 999.99")
    val price: Double? = null,
    val image: String? = null,
    @field:Min(value = 1, message = "Preparation time must be at least 1 minute")
    @field:Max(value = 300, message = "Preparation time must not exceed 300 minutes")
    val prepTimeMinutes: Int? = null,
)
