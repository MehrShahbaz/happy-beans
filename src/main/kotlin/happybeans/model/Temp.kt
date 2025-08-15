package happybeans.model

import happybeans.enums.TempType
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany
import org.apache.commons.lang3.mutable.Mutable

@Entity
class Temp(
    val type: TempType,
    val user: User? = null,
    val dish: Dish? = null,
    @OneToMany
    val tags: MutableList<Tag> = mutableListOf()
) {
    init {
        // Either User or dish should be populated
    }
}
