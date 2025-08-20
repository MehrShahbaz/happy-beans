package happybeans.dto.dish

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class DishUpdateRequest(
    @field:NotBlank(message = "Dish name is required")
    @field:Size(max = 100, message = "Dish name must not exceed 100 characters")
    val name: String,
    
    @field:NotBlank(message = "Description is required")
    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String,
    
    @field:NotBlank(message = "Image URL is required")
    val image: String
)