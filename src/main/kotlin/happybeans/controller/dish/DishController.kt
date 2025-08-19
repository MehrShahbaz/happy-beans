package happybeans.controller.dish

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.response.MessageResponse
import happybeans.model.Dish
import happybeans.service.DishService
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
@RequestMapping("/api/dish")
class DishController(
    private val dishService: DishService,
) {
    @GetMapping("/{dishId}")
    fun getDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<Dish> {
        val dish = dishService.findById(dishId)
        return ResponseEntity.ok(dish)
    }

    @GetMapping("/restaurant/{restaurantId}")
    fun getDishesByRestaurantId(
        @PathVariable restaurantId: Long,
        pageable: Pageable,
    ): ResponseEntity<List<Dish>> {
        val dishes = dishService.findByRestaurantId(restaurantId, pageable)
        return ResponseEntity.ok(dishes)
    }

    @PostMapping("/restaurant/{restaurantId}")
    fun createDish(
        @PathVariable restaurantId: Long,
        @Valid @RequestBody dishRequest: DishCreateRequest,
    ): ResponseEntity<MessageResponse> {
        val savedDish = dishService.createDish(restaurantId, dishRequest)

        val location: URI =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{dishId}")
                .buildAndExpand(savedDish.id)
                .toUri()

        return ResponseEntity.created(location).body(MessageResponse("Product added to cart"))
    }
}
