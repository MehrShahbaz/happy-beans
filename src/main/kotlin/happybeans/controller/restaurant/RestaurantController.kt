package happybeans.controller.restaurant

import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.model.Dish
import happybeans.model.Restaurant
import happybeans.service.RestaurantService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/restaurants")
class RestaurantController(
    private val restaurantService: RestaurantService,
) {
    @PostMapping
    fun createRestaurant(
        @Valid @RequestBody request: RestaurantCreateRequest,
    ): ResponseEntity<Restaurant> {
        val savedRestaurant = restaurantService.createRestaurant(request)

        val location: URI =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedRestaurant.id)
                .toUri()

        return ResponseEntity.created(location).body(savedRestaurant)
    }

    @GetMapping("/{restaurantId}")
    fun getRestaurantById(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Restaurant> {
        val restaurant = restaurantService.findById(restaurantId)
        return ResponseEntity.ok(restaurant)
    }

    @GetMapping("/{restaurantId}/dishes")
    fun getDishesByRestaurant(
        @PathVariable restaurantId: Long,
        pageable: Pageable,
    ): ResponseEntity<List<Dish>> {
        val dishes = restaurantService.findDishesByRestaurant(restaurantId, pageable)
        return ResponseEntity.ok(dishes)
    }
}
