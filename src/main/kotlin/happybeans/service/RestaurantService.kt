package happybeans.service

import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import org.springframework.stereotype.Service

@Service
class RestaurantService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
)
