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
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "tag_containers")
class TagContainer(
    @ManyToMany(cascade = [CascadeType.PERSIST, CascadeType.MERGE], fetch = FetchType.LAZY)
    @JoinTable(
        name = "tag_container_tags",
        joinColumns = [JoinColumn(name = "tag_container_id")],
        inverseJoinColumns = [JoinColumn(name = "tag_id")],
    )
    val tags: MutableSet<Tag> = mutableSetOf(),
    @Enumerated(EnumType.STRING)
    var type: TagContainerType,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,
    @ManyToOne(fetch = FetchType.LAZY)
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
        require((user == null) != (dish == null)) { "Exactly one of user or dish must be set" }
    }

    fun addTag(tag: Tag) {
        tags.add(tag)
    }

    fun addTags(newTags: Collection<Tag>) {
        tags.addAll(newTags)
    }

    fun removeTag(tag: Tag) {
        tags.remove(tag)
    }

    fun clearTags() {
        tags.clear()
    }

    fun getTagNames(): Set<String> = tags.map { it.name }.toSet()
}
