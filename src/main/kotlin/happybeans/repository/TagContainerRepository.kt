package happybeans.repository

import happybeans.enums.TagContainerType
import happybeans.model.TagContainer
import org.springframework.data.jpa.repository.JpaRepository

interface TagContainerRepository : JpaRepository<TagContainer, Long> {
    // TODO find by user id and type OR two methods which ever you like
    fun findByUserIdAndType(userId: Long, type: TagContainerType): TagContainer?
    // TODO find by dish id and hard code type Ingredients
    fun findByDishId(dishId: Long, type: TagContainerType): TagContainer?
}
