package happybeans.model

import happybeans.dto.restaurant.RestaurantPatchRequest
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
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "restaurants")
class Restaurant(
    @ManyToOne
    @JoinColumn(name = "user_id")
    var user: User,
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    @Column(name = "description", length = 500)
    var description: String,
    @Column(name = "image")
    var image: String,
    @Column(name = "address_url")
    var addressUrl: String,
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
        dishes.add(dish)
    }

    fun patchFields(patchOption: RestaurantPatchRequest) {
        patchOption.name?.let { name = it }
        patchOption.description?.let { description = it }
        patchOption.image?.let { image = it }
        patchOption.addressUrl?.let { addressUrl = it }
        patchOption.workingDateHours?.let { patch ->
            val byDay = workingDateHours.associateBy { it.dayOfWeek }
            patch.forEach { patchHour ->
                byDay[patchHour.dayOfWeek]?.apply {
                    openTime = patchHour.openTime
                    closeTime = patchHour.closeTime
                }
            }
        }
    }
}
