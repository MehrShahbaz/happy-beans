package happybeans.service

import happybeans.dto.restaurant.RestaurantResponseDto
import happybeans.dto.restaurant.toResponse
import happybeans.repository.RestaurantRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AdminRestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    private val logger = KotlinLogging.logger {}

    @Transactional(readOnly = true)
    fun getAllRestaurants(): List<RestaurantResponseDto> {
        logger.info("Get all restaurants for admin")
        return restaurantRepository.findAll().map { it.toResponse() }
    }
}
