package happybeans.model

import happybeans.enums.TagContainerType
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "tag_containers")
class TagContainer(
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_containers_id")
    val tags: MutableList<Tag> = mutableListOf(),
    @Enumerated(EnumType.STRING)
    var type: TagContainerType,
    @ManyToOne
    @JoinColumn(name = "users_id")
    var user: User? = null,
    @OneToOne
    @JoinColumn(name = "dish_id")
    var dish: Dish? = null,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    init {
        require((user == null && dish != null) || (user != null && dish == null)) { "Either user or dish must be set." }
    }
}
