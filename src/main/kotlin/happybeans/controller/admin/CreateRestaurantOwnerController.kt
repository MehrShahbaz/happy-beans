package happybeans.controller.admin

import happybeans.dto.response.MessageResponse
import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.service.HandleRestaurantOwnerCreateService
import jakarta.validation.Valid
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
    @PostMapping
    fun createRestaurantOwner(
        @Valid @RequestBody restaurantOwnerRequestDto: RestaurantOwnerRequestDto,
    ): ResponseEntity<MessageResponse> {
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
