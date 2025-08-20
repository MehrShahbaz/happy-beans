package happybeans.dto.dish

import jakarta.validation.constraints.Size

data class DishPatchRequest(
    @field:Size(max = 100, message = "Dish name must not exceed 100 characters")
    val name: String? = null,
    
    @field:Size(max = 500, message = "Description must not exceed 500 characters")
    val description: String? = null,
    
    val image: String? = null
)