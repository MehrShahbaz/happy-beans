package happybeans.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "dishes")
class Dish(
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "description", nullable = false)
    var description: String,
    @Column(name = "image", nullable = false)
    var image: String,
    @OneToMany(mappedBy = "dish", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var dishOption: MutableSet<DishOption> = mutableSetOf(),
    @Column(name = "average_rating")
    var averageRating: Double? = null,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    // if not used, can remove later : equals, hashCode, toString
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dish) return false
        return id != null && id == other.id
    }

    override fun hashCode(): Int = java.util.Objects.hash(id)

    override fun toString(): String = "Dish(id=$id, name='$name')"
}
