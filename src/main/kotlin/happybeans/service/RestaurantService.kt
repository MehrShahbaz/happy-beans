package happybeans.service

import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.dto.restaurant.WorkingDateHourRequest
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.model.WorkingDateHour
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.DuplicateEntityException
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
@Transactional
class RestaurantService(
    private val restaurantRepository: RestaurantRepository,
) {
    fun createRestaurant(
        restaurantCreateRequest: RestaurantCreateRequest,
        restaurantOwner: User,
    ): Restaurant {
        val currentRestaurants = restaurantRepository.findAllByUserId(restaurantOwner.id)

        if (currentRestaurants.any { it.name == restaurantCreateRequest.name }) {
            throw DuplicateEntityException("Owner already has a restaurant with name ${restaurantCreateRequest.name}")
        }

        val workingHours = createWorkingHours(restaurantCreateRequest.workingDateHours)

        val newRestaurant =
            Restaurant(
                restaurantOwner,
                restaurantCreateRequest.name,
                restaurantCreateRequest.description,
                restaurantCreateRequest.image,
                restaurantCreateRequest.addressUrl,
                workingHours.toMutableList(),
            )

        return restaurantRepository.save(newRestaurant)
    }

    fun patchRestaurant(
        restaurantPatchRequest: RestaurantPatchRequest,
        restaurantId: Long,
        userId: Long,
    ) {
        val restaurant = getRestaurantByIdAndOwnerId(restaurantId, userId)
        restaurant.patchFields(restaurantPatchRequest)
        restaurantRepository.save(restaurant)
    }

    fun deleteRestaurant(
        restaurantId: Long,
        userId: Long,
    ) {
        val restaurant = getRestaurantByIdAndOwnerId(restaurantId, userId)
        restaurantRepository.delete(restaurant)
    }

    fun getAllOwnedRestaurants(userId: Long): List<Restaurant> {
        return restaurantRepository.findAllByUserId(userId)
    }

    fun getRestaurantByIdAndOwnerId(
        restaurantId: Long,
        userId: Long,
    ): Restaurant {
        return restaurantRepository.findByIdAndUserId(restaurantId, userId) ?: throw EntityNotFoundException(
            "Resource not found for id: $restaurantId",
        )
    }

    private fun createWorkingHours(dto: List<WorkingDateHourRequest>): List<WorkingDateHour> {
        return dto.map {
            WorkingDateHour(
                it.dayOfWeek,
                it.openTime,
                it.closeTime,
            )
        }
    }
}
