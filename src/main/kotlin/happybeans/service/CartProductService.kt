package happybeans.service

import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductRequest
import happybeans.dto.cart.CartProductResponse
import happybeans.model.CartProduct
import happybeans.model.User
import happybeans.repository.CartProductRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
class CartProductService(
    private val cartProductRepository: CartProductRepository,
    private val dishService: DishService,
) {
    @Transactional
    fun clear(user: User) {
        logger.info { "Clearing cart for user: ${user.id}" }
        cartProductRepository.deleteAllByUserId(user.id)
        logger.info { "Cart for user: ${user.id} cleared successfully." }
    }

    @Transactional(readOnly = true)
    fun findAllByUserId(user: User): List<CartProduct> {
        logger.debug { "Finding all cart products for user: ${user.id}" }
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
        logger.info { "Attempting to add or update dish: $dishId, option: $optionId for user: ${user.id}" }
        val dishOption = dishService.findByIdAndDishOptionId(dishId, optionId)

        val product = cartProductRepository.findByUserIdAndDishOptionId(user.id, optionId)
        val cartProduct =
            if (product != null) {
                logger.debug { "Product already exists. Updating quantity from ${product.quantity} to ${req.quantity}." }
                product.apply { quantity = req.quantity }
            } else {
                logger.debug { "Product does not exist. Adding new product to cart." }
                CartProduct(
                    user = user,
                    dish = dish,
                    dishOption = dishOption,
                    quantity = req.quantity,
                )
            }

        cartProductRepository.save(cartProduct)
        logger.info { "Cart product for user: ${user.id} with dish: $dishId saved successfully." }
    }

    @Transactional
    fun deleteFromCart(
        user: User,
        dishOptionId: Long,
    ) {
        logger.info { "Deleting product from cart for user: ${user.id} with dish option: $dishOptionId" }
        cartProductRepository.deleteByUserIdAndDishOptionId(user.id, dishOptionId)
        logger.info { "Product with dish option: $dishOptionId deleted for user: ${user.id}" }
    }

    @Transactional
    fun updateQuantity(
        user: User,
        dishOptionId: Long,
        newQuantity: Int,
    ) {
        logger.info { "Updating quantity for user: ${user.id}, dish option: $dishOptionId to $newQuantity" }
        require(newQuantity > 0) { "Quantity must be greater than zero" }

        val cartProduct =
            cartProductRepository.findByUserIdAndDishOptionId(
                user.id,
                dishOptionId,
            ) ?: throw NoSuchElementException("Product not found")

        cartProduct.quantity = newQuantity
        cartProductRepository.save(cartProduct)
        logger.info { "Quantity for dish option: $dishOptionId updated successfully for user: ${user.id}" }
    }
}
