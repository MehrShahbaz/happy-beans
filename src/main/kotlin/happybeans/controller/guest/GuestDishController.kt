package happybeans.controller.guest

import happybeans.dto.dish.DishResponse
import happybeans.model.Restaurant
import happybeans.service.DishService
import happybeans.service.RestaurantService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/guest")
class GuestDishController(
    private val dishService: DishService,
    private val restaurantService: RestaurantService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{restaurantId}/dishes")
    fun getAllDishes(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<List<DishResponse>> {
        logger.info("GET Retrieving dishes for restaurantId: $restaurantId for guest")
        return ResponseEntity.ok(dishService.getAllDishes())
    }

    @GetMapping("/restaurant")
    fun getAllRestaurantService(): ResponseEntity<List<Restaurant>> {
        logger.info("GET Retrieving All restaurants for guest")
        return ResponseEntity.ok(restaurantService.getALlRestaurants())
    }
}
