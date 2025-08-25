package happybeans.repository

import happybeans.model.Restaurant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RestaurantRepository : JpaRepository<Restaurant, Long> {
    fun findByIdAndUserId(
        restaurantId: Long,
        ownerId: Long,
    ): Restaurant?

    fun findAllByUserId(userId: Long): List<Restaurant>
}
