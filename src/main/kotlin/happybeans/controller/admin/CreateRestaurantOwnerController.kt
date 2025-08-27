package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.service.HandleRestaurantOwnerCreateService
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/admin/restaurant-owner")
class CreateRestaurantOwnerController(
    private val service: HandleRestaurantOwnerCreateService,
) {
    private val logger = KotlinLogging.logger {}

    @PostMapping
    fun createRestaurantOwner(
        @Valid @RequestBody restaurantOwnerRequestDto: RestaurantOwnerRequestDto,
    ): ResponseEntity<MessageResponse> {
        logger.info("POST Admin creating a new owner request: ${restaurantOwnerRequestDto.email}")
        val user = service.handleCreateRestaurantOwner(restaurantOwnerRequestDto)

        val location =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("restaurant-owner")
                .buildAndExpand(user.id)
                .toUri()
        return ResponseEntity.created(location).body(MessageResponse("RestaurantOwner created!"))
    }
}
