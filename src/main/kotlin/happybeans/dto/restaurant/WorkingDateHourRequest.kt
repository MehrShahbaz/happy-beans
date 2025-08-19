package happybeans.dto.restaurant

import jakarta.validation.constraints.NotNull
import java.time.DayOfWeek
import java.time.LocalTime

data class WorkingDateHourRequest(
    @field:NotNull(message = "Day of week is required")
    val dayOfWeek: DayOfWeek,
    @field:NotNull(message = "Open time is required")
    val openTime: LocalTime,
    @field:NotNull(message = "Close time is required")
    val closeTime: LocalTime,
)
