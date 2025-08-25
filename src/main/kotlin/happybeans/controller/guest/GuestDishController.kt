package happybeans.controller.guest

import happybeans.model.Dish
import happybeans.service.DishService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/guest")
class GuestDishController(private val dishService: DishService) {
    @GetMapping("/{restaurantId}/dishes")
    fun getAllDishes(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<List<Dish>> {
        return ResponseEntity.ok(dishService.getAllDishes())
    }
}
