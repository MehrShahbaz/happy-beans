package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    fun findByName(name: String): Dish?

    fun findByNameOrNull(name: String): Dish?

    fun findByIdOrNull(id: Long): Dish?

    fun findAllOrNull(pageable: Pageable): List<Dish>?
}
