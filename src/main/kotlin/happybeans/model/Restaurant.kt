package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
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
import java.time.LocalTime

@Entity
@Table(name = "restaurants")
class Restaurant(
    @Column(name = "name", nullable = false)
    var name: String,
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    val category: List<Tag>,
    @Column(name = "image_url")
    var imageUrl: String? = null,
    @Column
    var openingHours: LocalTime,
    @Column
    var earliestArrival: Int,
//    @Column
//  var rating
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
    @ManyToOne
    @JoinColumn(name = "owner_id")
    val restaurantOwner: User,
    @OneToMany(mappedBy = "restaurant")
    val menu: List<Dish> = listOf(),

    val restaurantCategory: List<Tag>,
)
