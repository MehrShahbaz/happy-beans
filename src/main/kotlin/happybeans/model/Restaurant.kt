package happybeans.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity()
@Table(name = "restaurants")
class Restaurant(
    var name: String,
    var description: String,
    var image: String,
    // TODO Discussion
    var location: String,
    // TODO R&D
    var openHours: LocalDateTime,
    var closedHours: LocalDateTime,
    // TODO working days R&D
    var workingDays: List<String>,
    var prepTime: Int,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "restaurants_id")
    val dishes: MutableList<Dish> = mutableListOf(),
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
)
