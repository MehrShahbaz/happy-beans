package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

interface DishRepository : JpaRepository<Dish, Long> {
//    fun findByRestaurantId(
//        restaurantId: Long,
//        pageable: Pageable,
//    ): List<Dish>?

//    fun findAllOrNull(pageable: Pageable): Page<Dish>?
}
