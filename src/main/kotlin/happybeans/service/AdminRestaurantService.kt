package happybeans.service

import happybeans.enums.RestaurantStatus
import happybeans.model.Restaurant
import happybeans.repository.RestaurantRepository
import mu.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminRestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    private val logger = KotlinLogging.logger {}

    @Transactional(readOnly = true)
    fun getAllRestaurants(): List<Restaurant> {
        logger.info("Get all restaurants for admin")
        return restaurantRepository.findAll()
    }

    @Transactional
    fun deleteRestaurant(restaurantId: Long) {
        logger.info("DELETE delete restaurant by admin")
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
