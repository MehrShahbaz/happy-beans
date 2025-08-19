package happybeans.repository

import happybeans.model.Dish
import happybeans.model.Restaurant
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findById(
        restaurantId: Long,
        pageable: Pageable,
    ): List<Restaurant>

    fun findByName(name: String): Restaurant?

//    fun findDishesByRestaurant(
//        id: Long,
//        pageable: Pageable,
//    ): List<Dish>

//    fun findByIdOrNull(restaurantId: Long): Restaurant?

//    fun findAllOrNull(pageable: Pageable): List<Restaurant>?
}
