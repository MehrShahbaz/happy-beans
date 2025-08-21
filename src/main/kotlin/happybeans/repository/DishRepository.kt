package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.jpa.repository.JpaRepository

interface DishRepository : JpaRepository<Dish, Long> {
//    fun findByRestaurantId(
//        restaurantId: Long,
//        pageable: Pageable,
//    ): List<Dish>?

//    fun findAllOrNull(pageable: Pageable): Page<Dish>?
}
