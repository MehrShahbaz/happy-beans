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
}
