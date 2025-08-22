package happybeans.service

import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductRequest
import happybeans.dto.cart.CartProductResponse
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

    @Transactional(readOnly = true)
    fun findAllByUserId2(user: User): CartProductListResponse {
        return CartProductListResponse(findAllByUserId(user).map { CartProductResponse(it) })
    }

    @Transactional
    fun addOrUpdateCartProduct(
        user: User,
        ids: Pair<Long, Long>,
        req: CartProductRequest,
    ) {
        val (dishId, optionId) = ids

        val dish = dishService.findById(dishId)
        val dishOption = dishService.findByIdAndDishOptionId(dishId, optionId)

        val product =
            cartProductRepository.findByUserIdAndDishOptionId(user.id, optionId)
                ?.apply { quantity = req.quantity }
                ?: CartProduct(
                    user = user,
                    dish = dish,
                    dishOption = dishOption,
                    quantity = req.quantity,
                )

        cartProductRepository.save(product)
    }

    @Transactional
    fun deleteFromCart(
        user: User,
        dishOptionId: Long,
    ) {
        cartProductRepository.deleteByUserIdAndDishOptionId(user.id, dishOptionId)
    }

    @Transactional
    fun updateQuantity(
        user: User,
        dishOptionId: Long,
        newQuantity: Int,
    ) {
        require(newQuantity > 0) { "Quantity must be greater than zero" }

        val cartProduct =
            cartProductRepository.findByUserIdAndDishOptionId(
                user.id,
                dishOptionId,
            ) ?: throw NoSuchElementException("Product not found")

        cartProduct.quantity = newQuantity
        cartProductRepository.save(cartProduct)
    }
}
