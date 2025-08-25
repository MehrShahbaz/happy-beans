package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import java.time.DayOfWeek
import java.time.LocalTime

@Embeddable
data class WorkingDateHour(
    @Column(name = "day_of_week", nullable = false)
    @Enumerated(EnumType.STRING)
    val dayOfWeek: DayOfWeek,
    @Column(name = "open_time", nullable = false)
    var openTime: LocalTime,
    @Column(name = "close_time", nullable = false)
    var closeTime: LocalTime,
)
