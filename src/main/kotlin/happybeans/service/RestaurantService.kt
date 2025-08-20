package happybeans.service

import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.WorkingDateHourRequest
import happybeans.model.Restaurant
import happybeans.model.WorkingDateHour
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
class RestaurantService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
) {
    @Transactional
    fun createRestaurant(restaurantCreateRequest: RestaurantCreateRequest): Restaurant {
        val workingHours = createWorkingHours(restaurantCreateRequest.workingDateHours)

        val newRestaurant =
            Restaurant(
                name = restaurantCreateRequest.name,
                description = restaurantCreateRequest.description,
                image = restaurantCreateRequest.image,
                addressUrl = restaurantCreateRequest.addressUrl,
                workingDateHours = workingHours.toMutableList(),
            )

        return restaurantRepository.save(newRestaurant)
    }

    fun findById(id: Long): Restaurant {
        return restaurantRepository.findByIdOrNull(id)
            ?: throw EntityNotFoundException("Restaurant with id $id not found")
    }

    private fun createWorkingHours(dtos: List<WorkingDateHourRequest>): List<WorkingDateHour> {
        return dtos.map { dto ->
            WorkingDateHour(
                dayOfWeek = dto.dayOfWeek,
                openTime = dto.openTime,
                closeTime = dto.closeTime,
            )
        }
    }
}
