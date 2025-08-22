package happybeans.controller.member

import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductRequest
import happybeans.dto.response.MessageResponse
import happybeans.model.User
import happybeans.service.CartProductService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member/cart")
class CartProductController(
    private val cartProductService: CartProductService,
) {
    @GetMapping("")
    fun getCartProducts(
        @LoginMember user: User,
    ): ResponseEntity<CartProductListResponse> {
        return ResponseEntity.ok(cartProductService.findAllByUserId2(user))
    }

    @PostMapping("/dish/{dishId}/dish-option/{dishOptionId}")
    fun addToCart(
        @LoginMember user: User,
        @PathVariable("dishId") dishId: Long,
        @PathVariable("dishOptionId") dishOptionId: Long,
        @Valid @RequestBody cartProductRequest: CartProductRequest,
    ): ResponseEntity<MessageResponse> {
        cartProductService
            .addOrUpdateCartProduct(
                user,
                Pair(dishId, dishOptionId),
                cartProductRequest,
            )
        return ResponseEntity.ok(MessageResponse("Successfully added to cart!"))
    }

    @PatchMapping("/dish-option/{dishOptionId}")
    fun updateCartProductQuantity(
        @LoginMember user: User,
        @PathVariable("dishOptionId") dishOptionId: Long,
        @Valid @RequestBody request: CartProductRequest,
    ): ResponseEntity<MessageResponse> {
        cartProductService.updateQuantity(user, dishOptionId, request.quantity)
        return ResponseEntity.ok(MessageResponse("Option updated"))
    }

    @DeleteMapping("/dish-option/{dishOptionId}")
    fun deleteCartProduct(
        @LoginMember user: User,
        @PathVariable("dishOptionId") dishOptionId: Long,
    ): ResponseEntity<Unit> {
        cartProductService.deleteFromCart(user, dishOptionId)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("")
    fun clearCart(
        @LoginMember user: User,
    ): ResponseEntity<Unit> {
        cartProductService.clear(user)
        return ResponseEntity.noContent().build()
    }
}
