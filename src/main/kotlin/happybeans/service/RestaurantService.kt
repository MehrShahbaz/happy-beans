package happybeans.service

import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestaurantService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
) {
    @Transactional
    fun updateRestaurantPrepTime(restaurantId: Long) {
        val restaurant =
            restaurantRepository.findByIdOrNull(restaurantId)
                ?: throw EntityNotFoundException("Restaurant with id $restaurantId not found")
        val allDishes = restaurant.dishes
        if (allDishes.isEmpty()) {
            restaurant.prepTimeMinutes = 0
            return
        }
        val averagePrepTime =
            allDishes
                .map { it.prepTimeMinutes }
                .average()
                .toInt()
        restaurant.prepTimeMinutes = averagePrepTime
    }
}
