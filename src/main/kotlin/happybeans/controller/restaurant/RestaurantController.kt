package happybeans.controller.restaurant

import happybeans.dto.response.MessageResponse
import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.service.RestaurantService
import happybeans.utils.annotations.RestaurantOwner
import jakarta.validation.Valid
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
    @GetMapping("/{restaurantId}")
    fun getRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Restaurant> {
        return ResponseEntity.ok(restaurantService.getRestaurantByIdAndOwnerId(restaurantId, user.id))
    }

    @GetMapping
    fun getAllRestaurants(
        @RestaurantOwner user: User,
    ): ResponseEntity<List<Restaurant>> {
        return ResponseEntity.ok(restaurantService.getAllOwnedRestaurants(user.id))
    }

    @PostMapping
    fun createRestaurant(
        @RestaurantOwner user: User,
        @Valid @RequestBody request: RestaurantCreateRequest,
    ): ResponseEntity<MessageResponse> {
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
        restaurantService.patchRestaurant(request, restaurantId, user.id)
        return ResponseEntity.ok(MessageResponse("Created successfully!"))
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurantById(
        @RestaurantOwner user: User,
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Void> {
        restaurantService.deleteRestaurant(restaurantId, user.id)
        return ResponseEntity.noContent().build()
    }
}
