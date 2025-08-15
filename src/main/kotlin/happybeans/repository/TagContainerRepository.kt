package happybeans.repository

import happybeans.model.TagContainer
import org.springframework.data.jpa.repository.JpaRepository

interface TagContainerRepository : JpaRepository<TagContainer, Int> {
    // TODO find by user id and type OR two methods which ever you like
    // TODO find by dish id and hard code type Ingredients
}
