package happybeans.controller.dish

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.DishUpdateRequest
import happybeans.dto.dish.toResponse
import happybeans.dto.response.MessageResponse
import happybeans.model.User
import happybeans.service.DishService
import happybeans.utils.annotations.RestaurantOwner
import jakarta.validation.Valid
import mu.KotlinLogging
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
@RequestMapping("/api/restaurant-owner/")
class DishController(
    private val dishService: DishService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/dish/{dishId}")
    fun getDishById(
        @PathVariable dishId: Long,
    ): ResponseEntity<DishResponse> {
        logger.info("GET Get Dish by dishId: $dishId")
        val dish = dishService.findById(dishId)
        return ResponseEntity.ok(dish.toResponse())
    }

    @PostMapping("/restaurant/{restaurantId}/dishes")
    fun createDish(
        @RestaurantOwner owner: User,
        @PathVariable restaurantId: Long,
        @Valid @RequestBody dishRequest: DishCreateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Create Dish by restaurantId: $restaurantId Owner: ${owner.id}")
        val savedDish = dishService.createDish(restaurantId, dishRequest, owner)

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
        @RestaurantOwner owner: User,
        @PathVariable dishId: Long,
        @Valid @RequestBody updateRequest: DishUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PUT Update Dish by dishId: $dishId of owner: ${owner.id}")
        dishService.updateDish(dishId, updateRequest, owner)
        return ResponseEntity.ok(MessageResponse("Dish updated successfully"))
    }

    @PatchMapping("/dish/{dishId}")
    fun patchDish(
        @RestaurantOwner owner: User,
        @PathVariable dishId: Long,
        @Valid @RequestBody patchRequest: DishPatchRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PATCH Dish by dishId: $dishId of owner: ${owner.id}")
        dishService.patchDish(dishId, patchRequest, owner)
        return ResponseEntity.ok(MessageResponse("Dish updated successfully"))
    }

    @PostMapping("/dish/{dishId}/options")
    fun createDishOption(
        @RestaurantOwner owner: User,
        @PathVariable dishId: Long,
        @Valid @RequestBody optionRequest: DishOptionCreateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Create Option Dish by dishId: $dishId of owner: ${owner.id}")
        val dishOption = dishService.addDishOption(dishId, optionRequest, owner)
        val location: URI =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{optionId}")
                .buildAndExpand(dishOption.id)
                .toUri()
        return ResponseEntity.created(location).body(MessageResponse("Dish option created successfully"))
    }

    @DeleteMapping("/dish/{dishId}")
    fun deleteDishById(
        @RestaurantOwner owner: User,
        @PathVariable dishId: Long,
    ): ResponseEntity<Void> {
        logger.info("DELETE Option Dish by dishId: $dishId of owner: ${owner.id}")
        dishService.deleteDishById(dishId, owner)
        return ResponseEntity.noContent().build()
    }
}
