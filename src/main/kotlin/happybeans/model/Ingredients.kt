package happybeans.model

import happybeans.enums.TempType
import jakarta.persistence.OneToMany

class Ingredients(
    val dish: Dish? = null,
    @OneToMany
    val tags: MutableList<Tag> = mutableListOf()
)