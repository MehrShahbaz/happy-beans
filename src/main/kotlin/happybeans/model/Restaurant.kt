package happybeans.model

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
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

@Entity
@Table(name = "restaurants")
class Restaurant(
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    @Column(name = "description", length = 500)
    var description: String? = null,
    @Column(name = "image")
    var image: String,
    @Column(name = "address_url")
    var addressUrl: String,
    @Column(name = "address_url")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "restaurant_working_dates_hours",
        joinColumns = [JoinColumn(name = "restaurant_id")],
    )
    var workingDateHours: MutableList<WorkingDateHour> = mutableListOf(),
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "restaurant_id")
    val dishes: MutableList<Dish> = mutableListOf(),
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    fun addDish(dish: Dish) {
        this.dishes.add(dish)
    }

    // if not used, can remove later : equals, hashCode, toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Restaurant) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = java.util.Objects.hash(id)

    override fun toString(): String = "Restaurant(id=$id, name='$name')"
}

