package happybeans.repository

import happybeans.enums.TagContainerType
import happybeans.model.CartProduct
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.TagContainer
import happybeans.model.User
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class CartProductRepositoryTest {
    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var cartProductRepository: CartProductRepository

    private lateinit var user1: User
    private lateinit var user2: User
    private lateinit var dish: Dish
    private lateinit var dishOption1: DishOption
    private lateinit var dishOption2: DishOption
    private lateinit var dishOption3: DishOption

    @BeforeEach
    fun setUp() {
        user1 = User(email = "user1@example.com", password = "p1", firstName = "User", lastName = "One")
        user2 = User(email = "user2@example.com", password = "p2", firstName = "User", lastName = "Two")
        entityManager.persist(user1)
        entityManager.persist(user2)

        dish = Dish(name = "Pizza", description = "Cheesy pizza", image = "pizza.jpg")
        entityManager.persist(dish)

        val tagContainer = TagContainer(type = TagContainerType.INGREDIENTS, dish = dish)
        entityManager.persist(tagContainer)

        dishOption1 = DishOption(dish = dish, name = "Small", price = 10.0, image = "small.jpg", ingredients = tagContainer, rating = 4.0)
        dishOption2 = DishOption(dish = dish, name = "Large", price = 15.0, image = "large.jpg", ingredients = tagContainer, rating = 4.5)
        dishOption3 = DishOption(dish = dish, name = "Medium", price = 12.0, image = "medium.jpg", ingredients = tagContainer, rating = 4.2)
        entityManager.persist(dishOption1)
        entityManager.persist(dishOption2)
        entityManager.persist(dishOption3)

        val cartProduct1 = CartProduct(user1, dish, dishOption1)
        val cartProduct2 = CartProduct(user1, dish, dishOption2)
        val cartProduct3 = CartProduct(user2, dish, dishOption3)

        entityManager.persist(cartProduct1)
        entityManager.persist(cartProduct2)
        entityManager.persist(cartProduct3)

        entityManager.flush()
    }

    @Test
    fun `findAllByUserId should return products for a user`() {
        val foundProducts = cartProductRepository.findAllByUserId(user1.id)

        assertEquals(2, foundProducts.size)
        assertTrue(foundProducts.all { it.user.id == user1.id })
    }

    @Test
    fun `findByUserIdAndDishOptionId should find correct product`() {
        val foundProduct = cartProductRepository.findByUserIdAndDishOptionId(user1.id, dishOption1.id)

        assertNotNull(foundProduct)
        assertEquals(user1.id, foundProduct?.user?.id)
        assertEquals(dishOption1.id, foundProduct?.dishOption?.id)
    }

    @Test
    fun `findByUserIdAndDishOptionId should return null when not found`() {
        val foundProduct = cartProductRepository.findByUserIdAndDishOptionId(user2.id, dishOption2.id)

        assertNull(foundProduct)
    }

    @Test
    fun `deleteAllByUserId should remove all products for a user`() {
        cartProductRepository.deleteAllByUserId(user1.id)
        entityManager.flush()
        entityManager.clear()

        val remainingForUser1 = cartProductRepository.findAllByUserId(user1.id)
        val remainingForUser2 = cartProductRepository.findAllByUserId(user2.id)
        assertTrue(remainingForUser1.isEmpty())
        assertEquals(1, remainingForUser2.size)
    }

    @Test
    fun `deleteByUserIdAndDishOptionId should remove a specific product`() {
        cartProductRepository.deleteByUserIdAndDishOptionId(user1.id, dishOption1.id)
        entityManager.flush()
        entityManager.clear()

        val remainingForUser1 = cartProductRepository.findAllByUserId(user1.id)
        val productForUser2 = cartProductRepository.findByUserIdAndDishOptionId(user2.id, dishOption3.id)

        assertEquals(1, remainingForUser1.size)
        assertEquals(dishOption2.id, remainingForUser1[0].dishOption.id)
        assertNotNull(productForUser2)
    }

    @Test
    fun `should throw exception when persisting with zero quantity`() {
        val newCartProduct = CartProduct(user = user2, dish = dish, dishOption = dishOption1, quantity = 1)

        newCartProduct.quantity = 0

        assertThrows<IllegalArgumentException> {
            entityManager.persistAndFlush(newCartProduct)
        }
    }
}
