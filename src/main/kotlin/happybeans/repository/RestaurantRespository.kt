package happybeans.repository

import happybeans.model.Dish
import happybeans.model.Restaurant
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByRestaurantId(
        restaurantId: Long,
        pageable: Pageable,
    ): List<Restaurant>?

    fun findByIdOrNull(restaurantId: Long): Restaurant?

    fun findAllOrNull(pageable: Pageable): List<Restaurant>?
}
