package happybeans.controller.dish

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishUpdateRequest
import happybeans.dto.response.MessageResponse
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.service.DishService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping()
class DishController(
    private val dishService: DishService,
) {
    @GetMapping("/api/dish/{dishId}")
    fun getDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<Dish> {
        val dish = dishService.findById(dishId)
        return ResponseEntity.ok(dish)
    }

    @PostMapping("/api/restaurant/{restaurantId}/dishes")
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

        return ResponseEntity.created(location).body(MessageResponse("Dish created successfully"))
    }

    @PutMapping("/api/dish/{dishId}")
    fun updateDish(
        @PathVariable dishId: Long,
        @Valid @RequestBody updateRequest: DishUpdateRequest,
    ): ResponseEntity<Dish> {
        val updatedDish = dishService.updateDish(dishId, updateRequest)
        return ResponseEntity.ok(updatedDish)
    }

    @PatchMapping("/api/dish/{dishId}")
    fun patchDish(
        @PathVariable dishId: Long,
        @Valid @RequestBody patchRequest: DishPatchRequest,
    ): ResponseEntity<Dish> {
        val updatedDish = dishService.patchDish(dishId, patchRequest)
        return ResponseEntity.ok(updatedDish)
    }

    @PostMapping("/api/dish/{dishId}/options")
    fun addDishOption(
        @PathVariable dishId: Long,
        @Valid @RequestBody optionRequest: DishOptionCreateRequest,
    ): ResponseEntity<DishOption> {
        val dishOption = dishService.addDishOption(dishId, optionRequest)
        return ResponseEntity.ok(dishOption)
    }

    @PutMapping("/api/dish/{dishId}/options/{optionId}")
    fun updateDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
        @Valid @RequestBody updateRequest: DishOptionUpdateRequest,
    ): ResponseEntity<DishOption> {
        val updatedOption = dishService.updateDishOption(dishId, optionId, updateRequest)
        return ResponseEntity.ok(updatedOption)
    }

    @PatchMapping("/api/dish/{dishId}/options/{optionId}")
    fun patchDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
        @Valid @RequestBody patchRequest: DishOptionPatchRequest,
    ): ResponseEntity<DishOption> {
        val updatedOption = dishService.patchDishOption(dishId, optionId, patchRequest)
        return ResponseEntity.ok(updatedOption)
    }

    @DeleteMapping("/api/dish/{dishId}/options/{optionId}")
    fun deleteDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
    ): ResponseEntity<MessageResponse> {
        dishService.deleteDishOption(dishId, optionId)
        return ResponseEntity.ok(MessageResponse("Dish option deleted successfully"))
    }

    @DeleteMapping("/api/dish/{dishId}")
    fun deleteDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<MessageResponse> {
        dishService.deleteDishById(dishId)
        return ResponseEntity.ok(MessageResponse("Dish deleted successfully"))
    }
}
