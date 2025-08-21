package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "dish_options")
class DishOption(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    var dish: Dish,
    @Column(name = "name", nullable = false, length = 100)
    var name: String,
    @Column(name = "description", length = 500)
    var description: String? = null,
    @Column(name = "price", nullable = false)
    var price: Double,
    @Column(name = "image", nullable = false)
    var image: String,
    @Column(name = "available", nullable = false)
    var available: Boolean = true,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_containers_id")
    var ingredients: TagContainer,
    @Column(name = "prep_time_minute")
    var prepTimeMinutes: Int = 0,
    @Column(name = "rating", nullable = false)
    var rating: Double,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    // if not used, can remove later : equals, hashCode, toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DishOption) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = java.util.Objects.hash(id)

    override fun toString(): String = "DishOption(id=$id, name='$name')"
}
