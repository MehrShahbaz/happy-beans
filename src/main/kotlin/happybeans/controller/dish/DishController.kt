package happybeans.controller.dish

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.DishUpdateRequest
import happybeans.dto.dish.toResponse
import happybeans.dto.response.MessageResponse
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
@RequestMapping("/api")
class DishController(
    private val dishService: DishService,
) {
    @GetMapping("/dish/{dishId}")
    fun getDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<DishResponse> {
        val dish = dishService.findById(dishId)
        return ResponseEntity.ok(dish.toResponse())
    }

    @PostMapping("/restaurant/{restaurantId}/dishes")
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

    @PutMapping("/dish/{dishId}")
    fun updateDish(
        @PathVariable dishId: Long,
        @Valid @RequestBody updateRequest: DishUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.updateDish(dishId, updateRequest)
        return ResponseEntity.ok(MessageResponse("Dish updated successfully"))
    }

    @PatchMapping("/dish/{dishId}")
    fun patchDish(
        @PathVariable dishId: Long,
        @Valid @RequestBody patchRequest: DishPatchRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.patchDish(dishId, patchRequest)
        return ResponseEntity.ok(MessageResponse("Dish updated successfully"))
    }

    @PostMapping("/dish/{dishId}/options")
    fun addDishOption(
        @PathVariable dishId: Long,
        @Valid @RequestBody optionRequest: DishOptionCreateRequest,
    ): ResponseEntity<MessageResponse> {
        val dishOption = dishService.addDishOption(dishId, optionRequest)
        val location: URI =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{optionId}")
                .buildAndExpand(dishOption.id)
                .toUri()
        return ResponseEntity.created(location).body(MessageResponse("Dish option added successfully"))
    }

    @PutMapping("/dish/{dishId}/options/{optionId}")
    fun updateDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
        @Valid @RequestBody updateRequest: DishOptionUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.updateDishOption(dishId, optionId, updateRequest)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @PatchMapping("/dish/{dishId}/options/{optionId}")
    fun patchDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
        @Valid @RequestBody patchRequest: DishOptionPatchRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.patchDishOption(dishId, optionId, patchRequest)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @DeleteMapping("/dish/{dishId}/options/{optionId}")
    fun deleteDishOption(
        @PathVariable dishId: Long,
        @PathVariable optionId: Long,
    ): ResponseEntity<Void> {
        dishService.deleteDishOption(dishId, optionId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/dish/{dishId}")
    fun deleteDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<Void> {
        dishService.deleteDishById(dishId)
        return ResponseEntity.noContent().build()
    }
}
