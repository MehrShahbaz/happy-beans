package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.dto.restaurant.RestaurantStatusUpdateRequest
import happybeans.model.Restaurant
import happybeans.service.AdminRestaurantService
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
    @GetMapping
    fun getAllRestaurants(): ResponseEntity<List<Restaurant>> {
        return ResponseEntity.ok().body(adminRestaurantService.getAllRestaurants())
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurant(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Void> {
        adminRestaurantService.deleteRestaurant(restaurantId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{restaurantId}/status")
    fun updateRestaurantStatus(
        @PathVariable restaurantId: Long,
        @RequestBody request: RestaurantStatusUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        adminRestaurantService.updateRestaurantStatus(restaurantId, request.status)
        return ResponseEntity.ok(MessageResponse("Restaurant status is updated."))
    }
}
