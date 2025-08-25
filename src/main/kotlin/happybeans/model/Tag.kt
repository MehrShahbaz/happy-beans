package happybeans.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(
    name = "tags",
    indexes = [
        Index(name = "idx_tag_name", columnList = "name"),
    ],
)
class Tag(
    @Column(name = "name", unique = true, nullable = false)
    val name: String,
    @CreationTimestamp
    var createdAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Tag) return false
        return name == other.name
    }

    override fun hashCode(): Int = name.hashCode()

    override fun toString(): String = "Tag(name='$name')"
}
