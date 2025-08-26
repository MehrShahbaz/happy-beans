package happybeans.service

import happybeans.model.Restaurant
import happybeans.repository.RestaurantRepository
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
}
