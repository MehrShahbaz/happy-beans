package happybeans.controller.restaurant

import happybeans.dto.response.MessageResponse
import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.dto.restaurant.RestaurantResponseDto
import happybeans.dto.restaurant.toResponse
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
    ): ResponseEntity<RestaurantResponseDto> {
        logger.info("GET restaurant with id $restaurantId for user ${user.id}")
        return ResponseEntity.ok(restaurantService.getRestaurantByIdAndOwnerId(restaurantId, user.id).toResponse())
    }

    @GetMapping
    fun getAllRestaurants(
        @RestaurantOwner user: User,
    ): ResponseEntity<List<RestaurantResponseDto>> {
        logger.info("GET all restaurants for user ${user.id}")
        return ResponseEntity.ok(restaurantService.getAllOwnedRestaurants(user.id))
    }

    @PostMapping
    fun createRestaurant(
        @RestaurantOwner user: User,
        @Valid @RequestBody request: RestaurantCreateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST restaurant for owner ${user.id}")
        val savedRestaurant = restaurantService.createRestaurant(request, user)
        val uri = URI.create("/restaurants/${savedRestaurant.id}")
        return ResponseEntity.created(uri).body(MessageResponse("Created successfully!"))
    }

    @PatchMapping("/{restaurantId}")
    fun patchRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
        @Valid @RequestBody request: RestaurantPatchRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "PATCH restaurant $restaurantId for user ${user.id}" }
        restaurantService.patchRestaurant(request, restaurantId, user.id)
        return ResponseEntity.ok(MessageResponse("Patched successfully!"))
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Void> {
        logger.info { "DELETE restaurant $restaurantId for user ${user.id}" }
        restaurantService.deleteRestaurant(restaurantId, user.id)
        return ResponseEntity.noContent().build()
    }
}
