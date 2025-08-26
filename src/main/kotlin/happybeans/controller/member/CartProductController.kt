package happybeans.controller.member

import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductRequest
import happybeans.dto.response.MessageResponse
import happybeans.model.User
import happybeans.service.CartProductService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/member/cart")
class CartProductController(
    private val cartProductService: CartProductService,
) {
    @GetMapping("")
    fun getCartProducts(
        @LoginMember user: User,
    ): ResponseEntity<CartProductListResponse> {
        logger.info { "GET request received for user: ${user.id}'s cart." }
        val response = cartProductService.findAllByUserId2(user)
        logger.info { "Successfully retrieved cart products for user: ${user.id}." }
        return ResponseEntity.ok(response)
    }

    @PostMapping("/dish/{dishId}/dish-option/{dishOptionId}")
    fun addToCart(
        @LoginMember user: User,
        @PathVariable("dishId") dishId: Long,
        @PathVariable("dishOptionId") dishOptionId: Long,
        @Valid @RequestBody cartProductRequest: CartProductRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "POST request to add to cart for user: ${user.id}, dish: $dishId, option: $dishOptionId" }
        cartProductService
            .addOrUpdateCartProduct(
                user,
                Pair(dishId, dishOptionId),
                cartProductRequest,
            )
        logger.info { "Successfully added to cart for user: ${user.id}." }
        return ResponseEntity.ok(MessageResponse("Successfully added to cart!"))
    }

    @PatchMapping("/dish-option/{dishOptionId}")
    fun updateCartProductQuantity(
        @LoginMember user: User,
        @PathVariable("dishOptionId") dishOptionId: Long,
        @Valid @RequestBody request: CartProductRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "PATCH request to update quantity for user: ${user.id}, dish option: $dishOptionId." }
        cartProductService.updateQuantity(user, dishOptionId, request.quantity)
        logger.info { "Quantity updated for user: ${user.id}, dish option: $dishOptionId." }
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @DeleteMapping("/dish-option/{dishOptionId}")
    fun deleteCartProduct(
        @LoginMember user: User,
        @PathVariable("dishOptionId") dishOptionId: Long,
    ): ResponseEntity<Unit> {
        logger.info { "DELETE request to remove item for user: ${user.id}, dish option: $dishOptionId." }
        cartProductService.deleteFromCart(user, dishOptionId)
        logger.info { "Item with dish option: $dishOptionId deleted for user: ${user.id}." }
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("")
    fun clearCart(
        @LoginMember user: User,
    ): ResponseEntity<Unit> {
        logger.info { "DELETE request to clear cart for user: ${user.id}." }
        cartProductService.clear(user)
        logger.info { "Cart cleared for user: ${user.id}." }
        return ResponseEntity.noContent().build()
    }
}
