package happybeans.service

import happybeans.dto.cart.CartProductRequest
import happybeans.model.CartProduct
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.User
import happybeans.repository.CartProductRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import happybeans.model.TagContainer

@ExtendWith(MockKExtension::class)
class CartProductServiceTest {

    @InjectMockKs
    private lateinit var cartProductService: CartProductService

    @MockK
    private lateinit var cartProductRepository: CartProductRepository

    @MockK
    private lateinit var dishService: DishService

    @MockK
    private lateinit var tagContainer: TagContainer

    private lateinit var user: User
    private lateinit var dish: Dish
    private lateinit var dishOption: DishOption
    private lateinit var cartProduct: CartProduct

    @BeforeEach
    fun setUp() {
        user = User(
            id = 1,
            email = "test@example.com",
            password = "password",
            firstName = "Test",
            lastName = "User"
        )

        dish = Dish(
            id = 10,
            name = "Coffee",
            description = "A classic hot beverage.",
            image = "coffee.jpg"
        )

        dishOption = DishOption(
            id = 100,
            dish = dish,
            name = "Large",
            description = "A large cup of coffee.",
            price = 3.00, // Price is now a Double
            image = "large_coffee.jpg",
            ingredients = tagContainer,
            rating = 4.5
        )

        cartProduct = CartProduct(user, dish, dishOption).apply { quantity = 2 }
    }

    @Test
    fun `clear should delete all cart products for a user`() {
        every { cartProductRepository.deleteAllByUserId(user.id) } just runs

        cartProductService.clear(user)

        verify(exactly = 1) { cartProductRepository.deleteAllByUserId(user.id) }
    }

    @Test
    fun `findAllByUserId should return list of cart products`() {
        every { cartProductRepository.findAllByUserId(user.id) } returns listOf(cartProduct)

        val result = cartProductService.findAllByUserId(user)

        assertEquals(1, result.size)
        assertEquals(cartProduct, result[0])
        verify(exactly = 1) { cartProductRepository.findAllByUserId(user.id) }
    }

    @Test
    fun `addToCart should save a new product`() {
        val request = CartProductRequest(quantity = 3)
        val cartProductSlot = slot<CartProduct>()

        every { dishService.findById(dish.id) } returns dish
        every { dishService.findByIdAndDishOptionId(dish.id, dishOption.id) } returns dishOption
        every { cartProductRepository.save(capture(cartProductSlot)) } returns cartProduct

        cartProductService.addToCart(user, Pair(dish.id, dishOption.id), request)

        verify(exactly = 1) { cartProductRepository.save(any()) }

        val capturedProduct = cartProductSlot.captured
        assertEquals(user, capturedProduct.user)
        assertEquals(dish, capturedProduct.dish)
        assertEquals(dishOption, capturedProduct.dishOption)
        assertEquals(request.quantity, capturedProduct.quantity)
    }

    @Test
    fun `deleteFromCart should remove a product`() {
        every { cartProductRepository.deleteByUserIdAndDishOptionId(user.id, dishOption.id) } just runs

        cartProductService.deleteFromCart(user, dishOption.id)

        verify(exactly = 1) { cartProductRepository.deleteByUserIdAndDishOptionId(user.id, dishOption.id) }
    }

    @Test
    fun `updateQuantity should change quantity of an existing product`() {
        val newQuantity = 5
        val request = CartProductRequest(quantity = newQuantity)
        val cartProductSlot = slot<CartProduct>()

        every { cartProductRepository.findByUserIdAndDishOptionId(user.id, dishOption.id) } returns cartProduct
        every { cartProductRepository.save(capture(cartProductSlot)) } returns cartProduct

        cartProductService.updateQuantity(user, dishOption.id, request)

        verify(exactly = 1) { cartProductRepository.findByUserIdAndDishOptionId(user.id, dishOption.id) }
        verify(exactly = 1) { cartProductRepository.save(any()) }

        assertEquals(newQuantity, cartProductSlot.captured.quantity)
    }

    @Test
    fun `updateQuantity should throw exception when product not found`() {
        val nonExistentDishOptionId = 999L
        val request = CartProductRequest(quantity = 2)
        every { cartProductRepository.findByUserIdAndDishOptionId(user.id, nonExistentDishOptionId) } returns null

        assertThrows<NoSuchElementException> {
            cartProductService.updateQuantity(user, nonExistentDishOptionId, request)
        }
        verify(exactly = 0) { cartProductRepository.save(any()) }
    }

    @Test
    fun `updateQuantity should throw exception for zero quantity`() {
        val request = CartProductRequest(quantity = 0)

       assertThrows<IllegalArgumentException> {
            cartProductService.updateQuantity(user, dishOption.id, request)
        }
        verify(exactly = 0) { cartProductRepository.findByUserIdAndDishOptionId(any(), any()) }
        verify(exactly = 0) { cartProductRepository.save(any()) }
    }
}
