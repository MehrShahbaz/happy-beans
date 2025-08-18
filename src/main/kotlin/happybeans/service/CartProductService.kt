package happybeans.service

import happybeans.dto.cart.CartProductRequest
import happybeans.model.CartProduct
import happybeans.model.User
import happybeans.repository.CartProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartProductService(
    private val cartProductRepository: CartProductRepository,
    private val dishService: DishService,
) {
    @Transactional
    fun clear(user: User) {
        cartProductRepository.deleteAllByUserId(user.id)
    }

    @Transactional(readOnly = true)
    fun findAllByUserId(user: User): List<CartProduct> {
        return cartProductRepository.findAllByUserId(user.id)
    }

    @Transactional
    fun addToCart(
        user: User,
        request: CartProductRequest,
    ) {
        val dish = dishService.findById(request.dishId)
        val dishOption = dishService.findByDishIdAndOptionId(request.dishId, request.dishOptionId)
        cartProductRepository.save(
            CartProduct(
                user,
                dish,
                dishOption,
            ).apply {
                this.quantity = request.quantity
            },
        )
    }

    @Transactional
    fun updateQuantity(
        user: User,
        request: CartProductRequest,
    ) {
        require(request.quantity > 0) { "Quantity must be greater than zero" }

        val cartProduct =
            cartProductRepository.findByUserIdAndDishOptionId(
                user.id,
                request.dishOptionId,
            ) ?: throw NoSuchElementException("Product not found")

        cartProduct.quantity = request.quantity
        cartProductRepository.save(cartProduct)
    }
}
