package happybeans.controller.dish

import happybeans.model.Dish
import happybeans.service.DishService
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

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

    // TODO check it
//    @PostMapping("/restaurant/{restaurantId}")
//    fun createDish(
//        @PathVariable restaurantId: Long,
//        @Valid @RequestBody dishRequest: DishCreateRequest,
//    ): ResponseEntity<MessageResponse> {
//        val savedDish = dishService.createDish(restaurantId, dishRequest)
//
//        val location: URI = ServletUriComponentsBuilder
//            .fromCurrentRequest()
//            .path("/{dishId}")
//            .buildAndExpand(savedDish.id)
//            .toUri()
//
//        return ResponseEntity.created(URI.create("/restaurant/$restaurantId")).body(MessageResponse("Product added to cart"))
//    }
}
