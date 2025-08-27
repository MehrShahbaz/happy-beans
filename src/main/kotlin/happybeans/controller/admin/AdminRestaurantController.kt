package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.dto.restaurant.RestaurantStatusUpdateRequest
import happybeans.model.Restaurant
import happybeans.service.AdminRestaurantService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/admin/restaurants"])
class AdminRestaurantController(
    private val adminRestaurantService: AdminRestaurantService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getAllRestaurants(): ResponseEntity<List<Restaurant>> {
        logger.info("GET Getting all restaurants for Admin")
        return ResponseEntity.ok().body(adminRestaurantService.getAllRestaurants())
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurant(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Void> {
        logger.info("DELETE delete restaurant with restaurant id $restaurantId")
        adminRestaurantService.deleteRestaurant(restaurantId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{restaurantId}/status")
    fun updateRestaurantStatus(
        @PathVariable restaurantId: Long,
        @RequestBody request: RestaurantStatusUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PATCH update restaurant with restaurant id $restaurantId")
        adminRestaurantService.updateRestaurantStatus(restaurantId, request.status)
        return ResponseEntity.ok(MessageResponse("Restaurant status is updated."))
    }
}
