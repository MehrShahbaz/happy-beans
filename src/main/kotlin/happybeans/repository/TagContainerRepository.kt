package happybeans.repository

import happybeans.enums.TagContainerType
import happybeans.model.TagContainer
import org.springframework.data.jpa.repository.JpaRepository

interface TagContainerRepository : JpaRepository<TagContainer, Long> {
//    fun findByUserIdAndType(
//        userId: Long,
//        type: TagContainerType,
//    ): TagContainer?
//
//    fun findByDishIdAndType(
//        dishId: Long,
//        type: TagContainerType,
//    ): TagContainer?
}
