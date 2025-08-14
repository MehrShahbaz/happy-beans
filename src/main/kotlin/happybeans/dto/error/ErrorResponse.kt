package happybeans.dto.error

import java.time.Instant

class ErrorResponse(
    val timestamp: Instant = Instant.now(),
    val status: Int,
    val error: String,
    val message: Any,
    val path: String? = null,
)
