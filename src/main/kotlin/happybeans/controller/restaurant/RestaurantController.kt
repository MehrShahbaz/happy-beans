package happybeans.controller.restaurant

import happybeans.dto.response.MessageResponse
import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.service.RestaurantService
import happybeans.utils.annotations.RestaurantOwner
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/restaurant-owner/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{restaurantId}")
    fun getRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Restaurant> {
        logger.info("GET restaurant with id $restaurantId for user ${user.id}")
        logger.info("User details - ID: ${user.id}, Email: ${user.email}, Role: ${user.role}")

        return try {
            logger.info("Calling restaurantService.getRestaurantByIdAndOwnerId with restaurantId: $restaurantId, userId: ${user.id}")
            val restaurant = restaurantService.getRestaurantByIdAndOwnerId(restaurantId, user.id)
            logger.info("Successfully retrieved restaurant ${restaurant.id} for user ${user.id}")
            ResponseEntity.ok(restaurant)
        } catch (e: Exception) {
            logger.error("Error getting restaurant $restaurantId for user ${user.id}: ${e.message}", e)
            throw e
        }
    }

    @GetMapping
    fun getAllRestaurants(
        @RestaurantOwner user: User,
    ): ResponseEntity<List<Restaurant>> {
        logger.info("GET all restaurants for user ${user.id}")
        logger.info("User details - ID: ${user.id}, Email: ${user.email}, Role: ${user.role}")

        return try {
            logger.info("Calling restaurantService.getAllOwnedRestaurants with userId: ${user.id}")
            val restaurants = restaurantService.getAllOwnedRestaurants(user.id)
            logger.info("Successfully retrieved ${restaurants.size} restaurants for user ${user.id}")
            ResponseEntity.ok(restaurants)
        } catch (e: Exception) {
            logger.error("Error getting restaurants for user ${user.id}: ${e.message}", e)
            throw e
        }
    }

    @PostMapping
    fun createRestaurant(
        @RestaurantOwner user: User,
        @Valid @RequestBody request: RestaurantCreateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST restaurant for owner ${user.id}")
        logger.info("User details - ID: ${user.id}, Email: ${user.email}, Role: ${user.role}")
        logger.info("Restaurant creation request: ${request.name} for owner ${user.id}")

        return try {
            logger.info("Calling restaurantService.createRestaurant for user ${user.id}")
            val savedRestaurant = restaurantService.createRestaurant(request, user)
            logger.info("Successfully created restaurant ${savedRestaurant.id} for owner ${user.id}")
            val uri = URI.create("/restaurants/${savedRestaurant.id}")
            ResponseEntity.created(uri).body(MessageResponse("Created successfully!"))
        } catch (e: Exception) {
            logger.error("Error creating restaurant for user ${user.id}: ${e.message}", e)
            throw e
        }
    }

    @PatchMapping("/{restaurantId}")
    fun patchRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
        @Valid @RequestBody request: RestaurantPatchRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PATCH restaurant: $restaurantId for user ${user.id}")
        restaurantService.patchRestaurant(request, restaurantId, user.id)
        return ResponseEntity.ok(MessageResponse("Patched successfully!"))
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Void> {
        logger.info("DELETE restaurant: $restaurantId for user ${user.id}")
        restaurantService.deleteRestaurant(restaurantId, user.id)
        return ResponseEntity.noContent().build()
    }
}
