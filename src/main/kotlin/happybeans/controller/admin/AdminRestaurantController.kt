package happybeans.controller.admin

import happybeans.model.Restaurant
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
    fun getAllRestaurants(): ResponseEntity<List<Restaurant>> {
        logger.info("GET Getting all restaurants for Admin")
        return ResponseEntity.ok().body(adminRestaurantService.getAllRestaurants())
    }
}
