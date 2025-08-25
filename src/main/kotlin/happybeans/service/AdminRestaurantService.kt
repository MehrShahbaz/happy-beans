package happybeans.service

import happybeans.enums.RestaurantStatus
import happybeans.model.Restaurant
import happybeans.repository.RestaurantRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminRestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    @Transactional(readOnly = true)
    fun getAllRestaurants(): List<Restaurant> {
        return restaurantRepository.findAll()
    }

    @Transactional
    fun deleteRestaurant(restaurantId: Long) {
        val restaurant =
            restaurantRepository.findByIdOrNull(restaurantId)
                ?: throw IllegalArgumentException("Restaurant not found with id: $restaurantId")
        restaurantRepository.delete(restaurant)
    }

    @Transactional
    fun updateRestaurantStatus(
        restaurantId: Long,
        status: RestaurantStatus,
    ): Restaurant {
        val restaurant =
            restaurantRepository.findByIdOrNull(restaurantId)
                ?: throw IllegalArgumentException("Restaurant not found with id: $restaurantId")
        restaurant.status = status
        return restaurantRepository.save(restaurant)
    }
}
