package happybeans.service

import happybeans.TestFixture
import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.model.Dish
import happybeans.model.Restaurant
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.DishAlreadyExistsException
import happybeans.utils.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class DishServiceTest {
    @Mock
    private lateinit var dishRepository: DishRepository

    @Mock
    private lateinit var restaurantRepository: RestaurantRepository

    @InjectMocks
    private lateinit var dishService: DishService

    @Test
    fun `findByName should return dish when found`() {
        // Given
        val dishName = "Margherita Pizza"
        val expectedDish = TestFixture.createMargheritaPizza()
        given(dishRepository.findByName(dishName)).willReturn(expectedDish)

        // When
        val result = dishService.findByName(dishName)

        // Then
        assertThat(result).isEqualTo(expectedDish)
    }

    @Test
    fun `findByName should return null when dish not found`() {
        // Given
        val dishName = "Non-existent Dish"
        given(dishRepository.findByName(dishName)).willReturn(null)

        // When
        val result = dishService.findByName(dishName)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `findById should return dish when found`() {
        // Given
        val dishId = 1L
        val expectedDish = TestFixture.createMargheritaPizza()
        given(dishRepository.findByIdOrNull(dishId)).willReturn(expectedDish)

        // When
        val result = dishService.findById(dishId)

        // Then
        assertThat(result).isEqualTo(expectedDish)
    }

    @Test
    fun `findById should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        given(dishRepository.findByIdOrNull(dishId)).willReturn(null)

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.findById(dishId)
        }
    }

    @Test
    fun `createDish should create dish with options successfully`() {
        // Given
        val restaurantId = 1L
        val restaurant: Restaurant = TestFixture.createHappyBeansCafe()
        val dishRequest =
            DishCreateRequest(
                name = "Test Pizza",
                description = "A delicious test pizza",
                image = "https://example.com/pizza.jpg",
                dishOptionRequests =
                    mutableSetOf(
                        DishOptionCreateRequest(
                            name = "Small Pizza",
                            description = "Perfect for one person",
                            price = 15.99,
                            image = "https://example.com/small-pizza.jpg",
                            prepTimeMinutes = 20,
                            rating = 4.5,
                        ),
                    ),
            )

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant))
        given(dishRepository.findByName("Test Pizza")).willReturn(null) // No existing dish
        given(dishRepository.save(org.mockito.ArgumentMatchers.any(Dish::class.java)))
            .willAnswer { it.arguments[0] as Dish }

        // When
        val result = dishService.createDish(restaurantId, dishRequest)

        // Then
        assertThat(result.name).isEqualTo("Test Pizza")
        assertThat(result.description).isEqualTo("A delicious test pizza")
        assertThat(result.dishOptions).hasSize(1)

        val dishOption = result.dishOptions.first()
        assertThat(dishOption.name).isEqualTo("Small Pizza")
        assertThat(dishOption.price).isEqualTo(15.99)
        assertThat(dishOption.dish).isEqualTo(result)

        verify(dishRepository).save(result)
    }

    @Test
    fun `createDish should throw EntityNotFoundException when restaurant not found`() {
        // Given
        val restaurantId = 999L
        val dishRequest =
            DishCreateRequest(
                name = "Test Pizza",
                description = "A delicious test pizza",
                image = "https://example.com/pizza.jpg",
                dishOptionRequests = mutableSetOf(),
            )

        given(restaurantRepository.findById(restaurantId)).willReturn(null)

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.createDish(restaurantId, dishRequest)
        }
    }

    @Test
    fun `createDish should create dish without options when no options provided`() {
        // Given
        val restaurantId = 1L
        val restaurant = TestFixture.createHappyBeansCafe()
        val dishRequest =
            DishCreateRequest(
                name = "Simple Dish",
                description = "A simple dish",
                image = "https://example.com/dish.jpg",
                dishOptionRequests = mutableSetOf(),
            )

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant))
        given(dishRepository.save(org.mockito.ArgumentMatchers.any(Dish::class.java)))
            .willAnswer { it.arguments[0] as Dish }

        // When
        val result = dishService.createDish(restaurantId, dishRequest)

        // Then
        assertThat(result.name).isEqualTo("Simple Dish")
        assertThat(result.dishOptions).isEmpty()
        verify(dishRepository).save(result)
    }

    @Test
    fun `createDish should add dish to restaurant`() {
        // Given
        val restaurantId = 1L
        val restaurant = TestFixture.createHappyBeansCafe()
        val initialDishCount = restaurant.dishes.size
        val dishRequest =
            DishCreateRequest(
                name = "New Dish",
                description = "A new dish",
                image = "https://example.com/new-dish.jpg",
                dishOptionRequests = mutableSetOf(),
            )

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant))
        given(dishRepository.save(org.mockito.ArgumentMatchers.any(Dish::class.java)))
            .willAnswer { it.arguments[0] as Dish }

        // When
        val result = dishService.createDish(restaurantId, dishRequest)

        // Then
        assertThat(restaurant.dishes).hasSize(initialDishCount + 1)
        assertThat(restaurant.dishes).contains(result)
    }

    @Test
    fun `createDish should throw DishAlreadyExistsException when dish name already exists`() {
        // Given
        val restaurantId = 1L
        val restaurant = TestFixture.createHappyBeansCafe()
        val existingDish = TestFixture.createMargheritaPizza()
        val dishRequest =
            DishCreateRequest(
                name = "Margherita Pizza",
                description = "Another pizza with the same name",
                image = "https://example.com/another-pizza.jpg",
                dishOptionRequests = mutableSetOf(),
            )

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.of(restaurant))
        given(dishRepository.findByName("Margherita Pizza")).willReturn(existingDish) // Dish already exists

        // When & Then
        val exception =
            assertThrows<DishAlreadyExistsException> {
                dishService.createDish(restaurantId, dishRequest)
            }

        assertThat(exception.message).isEqualTo("Dish with name 'Margherita Pizza' already exists")

        // Verify that save was never called
        verify(dishRepository, org.mockito.Mockito.never()).save(org.mockito.ArgumentMatchers.any(Dish::class.java))
    }
}
