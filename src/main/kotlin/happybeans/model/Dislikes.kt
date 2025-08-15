package happybeans.model

import happybeans.enums.TempType
import jakarta.persistence.OneToMany

class Dislikes(
    val user: User? = null,
    @OneToMany
    val tags: MutableList<Tag> = mutableListOf()
)