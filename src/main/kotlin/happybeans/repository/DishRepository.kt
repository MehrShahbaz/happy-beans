package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    fun findByName(name: String): Dish?
}
