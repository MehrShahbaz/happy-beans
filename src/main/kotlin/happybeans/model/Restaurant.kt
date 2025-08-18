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

@Entity()
@Table(name = "restaurants")
class Restaurant(
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    @Column(name = "description", length = 500)
    var description: String? = null,
    @Column(name = "image")
    var image: String,
    @Column(name = "address_url")
    var addressUrl: String?,
    @Column(name = "address_ulr")
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
        name = "restaurant_working_dates_hours",
        joinColumns = [JoinColumn(name = "restaurant_id")],
    )
    var workingDateHours: MutableList<WorkingDateHour> = mutableListOf(),
    @Column(name = "prep_time_minutes")
    var prepTimeMinutes: Int = 0,
    @OneToMany(cascade = [(CascadeType.ALL)], mappedBy = "restaurant", fetch = FetchType.LAZY, orphanRemoval = true)
    val dishes: MutableList<Dish> = mutableListOf(),
    @Column(name = "restaurant_rating", nullable = false)
    var restaurantRating: Double,
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
        return id != null && id == other.id
    }

    override fun hashCode(): Int = java.util.Objects.hash(id)

    override fun toString(): String = "Restaurant(id=$id, name='$name')"
}

/* TODO: decide to implement "calculating location for recommendation feature"
: it can be one additional entity : class locationInfo
    @Column(name = "address", nullable = false)
    var address: String,

@Column(name = "postal_code")
var postalCode: String? = null,

@Column(name = "latitude", precision = 10, scale = 8)
var latitude: BigDecimal? = null,

@Column(name = "longitude", precision = 11, scale = 8)
var longitude: BigDecimal? = null,
*/
