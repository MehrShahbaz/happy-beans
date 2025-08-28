package happybeans.controller.admin

import happybeans.dto.restaurant.RestaurantResponseDto
import happybeans.service.AdminRestaurantService
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/admin/restaurants"])
class AdminRestaurantController(
    private val adminRestaurantService: AdminRestaurantService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping
    fun getAllRestaurants(): ResponseEntity<List<RestaurantResponseDto>> {
        logger.info("GET Getting all restaurants for Admin")
        val temp = adminRestaurantService.getAllRestaurants()
        return ResponseEntity.ok().body(adminRestaurantService.getAllRestaurants())
    }
}
