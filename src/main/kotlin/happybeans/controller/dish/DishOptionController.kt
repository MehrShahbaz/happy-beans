package happybeans.controller.dish

import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionResponse
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.toResponse
import happybeans.dto.response.MessageResponse
import happybeans.dto.tag.TagRequest
import happybeans.model.Tag
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

@RestController
@RequestMapping("/api/restaurant-owner/dish-options")
class DishOptionController(
    private val dishService: DishService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/{optionId}")
    fun getDishOptionById(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
    ): ResponseEntity<DishOptionResponse> {
        logger.info("GET Get dish option by optionId: $optionId")
        val dishOption = dishService.findDishOptionById(optionId)
        return ResponseEntity.ok(dishOption.toResponse())
    }

    @PutMapping("/{optionId}")
    fun updateDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody updateRequest: DishOptionUpdateRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PUT Updating dish option for $optionId and user ${owner.id}")
        dishService.updateDishOption(optionId, updateRequest, owner)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @PatchMapping("/{optionId}")
    fun patchDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody patchRequest: DishOptionPatchRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("PATCH Patching dish option for $optionId and user ${owner.id}")
        dishService.patchDishOption(optionId, patchRequest, owner)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @DeleteMapping("/{optionId}")
    fun deleteDishOption(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
    ): ResponseEntity<Void> {
        logger.info("DELETE Deleting dish option for $optionId and user ${owner.id}")
        dishService.deleteDishOption(optionId, owner)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{optionId}/tags")
    fun getDishOptionTags(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
    ): ResponseEntity<Set<Tag>> {
        logger.info("GET Get dish option tags for $optionId}")
        val tags = dishService.getDishOptionTags(optionId)
        return ResponseEntity.ok(tags)
    }

    @PostMapping("/{optionId}/tags")
    fun addDishOptionTag(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Add tag for dish option for $optionId}")
        val tagName =
            tagRequest.tagNames.firstOrNull() ?: run {
                logger.error("tagNames is empty in request: $tagRequest")
                throw IllegalArgumentException("At least one tag name is required")
            }
        dishService.addDishOptionTag(optionId, tagName, owner)
        return ResponseEntity.ok(MessageResponse("Tag added successfully"))
    }

    @DeleteMapping("/{optionId}/tags")
    fun removeDishOptionTag(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        val tagName =
            tagRequest.tagNames.firstOrNull() ?: run {
                logger.error("tagNames is empty in request: $tagRequest")
                throw IllegalArgumentException("At least one tag name is required")
            }
        logger.info("DELETE Tag for $optionId and user ${owner.id}")
        dishService.removeDishOptionTag(optionId, tagName, owner)
        return ResponseEntity.ok(MessageResponse("Tag removed successfully"))
    }

    @PutMapping("/{optionId}/tags")
    fun updateDishOptionTags(
        @RestaurantOwner owner: User,
        @PathVariable optionId: Long,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        val tagNames = tagRequest.tagNames.toSet()
        logger.info("UPDATE Tags for $optionId and user ${owner.id}")
        dishService.updateDishOptionTags(optionId, tagNames, owner)
        return ResponseEntity.ok(MessageResponse("Tags updated successfully"))
    }
}
