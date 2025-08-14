package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "dish_options")
class DishOption(
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "image_url", nullable = false)
    var imageUrl: String,
    @Column(name = "description", nullable = false)
    var description: String,
    @Column(name = "price", nullable = false)
    var price: Int,
    @Column(name = "price", nullable = false)
    var stock: Int,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
//    @Column
//    var rating
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    val ingredients: List<Tag>,
    val flavorProfiles: List<Tag>,
)
