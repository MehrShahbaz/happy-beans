package happybeans.dto.tag

import jakarta.validation.constraints.NotEmpty

data class TagRequest(
    @field:NotEmpty(message = "Tag names cannot be empty")
    val tagNames: List<String>,
)
