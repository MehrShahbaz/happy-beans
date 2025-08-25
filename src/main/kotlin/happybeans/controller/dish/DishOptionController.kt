package happybeans.controller.dish

import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.response.MessageResponse
import happybeans.model.Tag
import happybeans.model.User
import happybeans.service.DishService
import happybeans.utils.annotations.RestaurantOwner
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

@RestController
@RequestMapping("/api/restaurant-owner/dish-options")
class DishOptionController(
    private val dishService: DishService,
) {
    @PutMapping("/{optionId}")
    fun updateDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody updateRequest: DishOptionUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.updateDishOption(optionId, updateRequest, owner)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @PatchMapping("/{optionId}")
    fun patchDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody patchRequest: DishOptionPatchRequest,
    ): ResponseEntity<MessageResponse> {
        dishService.patchDishOption(optionId, patchRequest, owner)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @DeleteMapping("/{optionId}")
    fun deleteDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
    ): ResponseEntity<Void> {
        dishService.deleteDishOption(optionId, owner)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{optionId}/tags")
    fun getDishOptionTags(
        @PathVariable optionId: Long,
    ): ResponseEntity<Set<Tag>> {
        val tags = dishService.getDishOptionTags(optionId)
        return ResponseEntity.ok(tags)
    }

    @PostMapping("/{optionId}/tags")
    fun addDishOptionTag(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        dishService.addDishOptionTag(optionId, tagName, owner)
        return ResponseEntity.ok(MessageResponse("Tag added successfully"))
    }

    @DeleteMapping("/{optionId}/tags")
    fun removeDishOptionTag(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        dishService.removeDishOptionTag(optionId, tagName, owner)
        return ResponseEntity.ok(MessageResponse("Tag removed successfully"))
    }

    @PutMapping("/{optionId}/tags")
    fun updateDishOptionTags(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @RequestBody tagRequest: Map<String, Set<String>>,
    ): ResponseEntity<MessageResponse> {
        val tagNames = tagRequest["tagNames"] ?: throw IllegalArgumentException("tagNames is required")
        dishService.updateDishOptionTags(optionId, tagNames, owner)
        return ResponseEntity.ok(MessageResponse("Tags updated successfully"))
    }
}
