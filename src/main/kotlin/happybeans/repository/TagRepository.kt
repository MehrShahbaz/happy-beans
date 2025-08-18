package happybeans.repository

import happybeans.model.Tag
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository : JpaRepository<Tag, Long> {
    fun findByNameOrNull(name: String): Tag?
}
