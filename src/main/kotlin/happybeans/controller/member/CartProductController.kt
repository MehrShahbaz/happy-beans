package happybeans.controller.member

import happybeans.dto.cart.CartProductRequest
import happybeans.model.User
import happybeans.service.CartProductService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/api/member")
class CartProductController(private val cartProductService: CartProductService) {
    @GetMapping("/cart")
    fun getCartProducts(
        @LoginMember user: User,
        model: Model,
    ): String {
        model.addAttribute(
            "cartProducts",
            cartProductService.findAllByUserId(user),
        )
        return "cart"
    }

    @PostMapping("/cart")
    fun addToCart(
        @LoginMember user: User,
        @Valid @RequestBody cartProductRequest: CartProductRequest,
    ): String {
        cartProductService.addToCart(user, cartProductRequest)
        return "redirect:/api/member/cart"
    }

    @PatchMapping("/cart")
    fun updateCartProductQuantity(
        @LoginMember user: User,
        @Valid @RequestBody request: CartProductRequest,
    ): String {
        cartProductService.updateQuantity(user, request)
        return "redirect:/api/member/cart"
    }

    @DeleteMapping("/cart")
    fun clearCart(
        @LoginMember user: User,
    ): String {
        cartProductService.clear(user)
        return "redirect:/api/member/cart"
    }
}
