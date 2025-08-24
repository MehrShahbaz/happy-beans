package happybeans.service

import happybeans.model.Restaurant
import happybeans.repository.RestaurantRepository
import org.springframework.stereotype.Service

@Service
class AdminRestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    fun getAllRestaurants(): List<Restaurant> {
        return restaurantRepository.findAll()
    }
}
