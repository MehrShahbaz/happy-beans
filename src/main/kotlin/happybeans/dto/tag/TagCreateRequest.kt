package happybeans.dto.tag

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class TagCreateRequest(
    @field:NotBlank(message = "Tag name is required")
    @field:Size(min = 1, max = 50, message = "Tag name must be between 1 and 50 characters")
    val name: String,
)
