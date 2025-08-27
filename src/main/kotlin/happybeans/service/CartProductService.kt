package happybeans.service

import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductRequest
import happybeans.dto.cart.CartProductResponse
import happybeans.model.CartProduct
import happybeans.model.User
import happybeans.repository.CartProductRepository
import happybeans.utils.exception.EntityNotFoundException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CartProductService(
    private val cartProductRepository: CartProductRepository,
    private val dishService: DishService,
) {
    private val logger = KotlinLogging.logger {}

    @Transactional
    fun clear(user: User) {
        logger.info { "Clearing cart for user: ${user.id}" }
        cartProductRepository.deleteAllByUserId(user.id)
    }

    @Transactional(readOnly = true)
    fun findAllByUserId(user: User): List<CartProduct> {
        logger.debug { "Finding all cart products for user: ${user.id}" }
        return cartProductRepository.findAllByUserId(user.id)
    }

    @Transactional(readOnly = true)
    fun findAllByUserId2(user: User): CartProductListResponse {
        logger.debug { "Finding all cart products for user: ${user.id}" }
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

        if (!dishOption.available) {
            logger.error("Cannot add unavailable dish for user: ${user.id} dishId: $dishId")
            throw EntityNotFoundException("Dish option '${dishOption.name}' is currently unavailable")
        }

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
        logger.info { "Deleting product from cart for user: ${user.id} with dish option: $dishOptionId" }
        cartProductRepository.deleteByUserIdAndDishOptionId(user.id, dishOptionId)
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
    }
}
