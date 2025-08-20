package happybeans.repository

import happybeans.model.Restaurant
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByName(name: String): Restaurant?

    fun findByIdOrNull(restaurantId: Long): Restaurant?

    fun findAllOrNull(pageable: Pageable): List<Restaurant>?
}
