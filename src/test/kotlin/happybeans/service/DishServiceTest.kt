package happybeans.service

import happybeans.TestFixture
import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishUpdateRequest
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
        given(dishRepository.findById(dishId)).willReturn(Optional.of(expectedDish))

        // When
        val result = dishService.findById(dishId)

        // Then
        assertThat(result).isEqualTo(expectedDish)
    }

    @Test
    fun `findById should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

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
        given(dishRepository.findByNameAndRestaurantId("Test Pizza", restaurantId)).willReturn(null)
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

        given(restaurantRepository.findById(restaurantId)).willReturn(Optional.empty())

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
        given(dishRepository.findByNameAndRestaurantId("Simple Dish", restaurantId)).willReturn(null)
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
        given(dishRepository.findByNameAndRestaurantId("New Dish", restaurantId)).willReturn(null)
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
        given(dishRepository.findByNameAndRestaurantId("Margherita Pizza", restaurantId)).willReturn(existingDish)

        // When & Then
        val exception =
            assertThrows<DishAlreadyExistsException> {
                dishService.createDish(restaurantId, dishRequest)
            }

        assertThat(exception.message).isEqualTo("Dish with name 'Margherita Pizza' already exists in this restaurant with id '1'")

        // Verify that save was never called
        verify(dishRepository, org.mockito.Mockito.never()).save(org.mockito.ArgumentMatchers.any(Dish::class.java))
    }

    @Test
    fun `createDish should allow same dish name in different restaurants`() {
        // Given
        val restaurant1Id = 1L
        val restaurant2Id = 2L
        val restaurant1 = TestFixture.createHappyBeansCafe()
        val restaurant2 = TestFixture.createMammaMiaPizzeria()
        val dishRequest =
            DishCreateRequest(
                name = "Margherita Pizza",
                description = "Classic Italian pizza",
                image = "https://example.com/pizza.jpg",
                dishOptionRequests = mutableSetOf(),
            )

        given(restaurantRepository.findById(restaurant1Id)).willReturn(Optional.of(restaurant1))
        given(restaurantRepository.findById(restaurant2Id)).willReturn(Optional.of(restaurant2))
        given(dishRepository.findByNameAndRestaurantId("Margherita Pizza", restaurant1Id)).willReturn(null)
        given(dishRepository.findByNameAndRestaurantId("Margherita Pizza", restaurant2Id)).willReturn(null)
        given(dishRepository.save(org.mockito.ArgumentMatchers.any(Dish::class.java)))
            .willAnswer { it.arguments[0] as Dish }

        // When - Create same dish name for different restaurants
        val result1 = dishService.createDish(restaurant1Id, dishRequest)
        val result2 = dishService.createDish(restaurant2Id, dishRequest)

        // Then - Both should succeed
        assertThat(result1.name).isEqualTo("Margherita Pizza")
        assertThat(result2.name).isEqualTo("Margherita Pizza")
        assertThat(restaurant1.dishes).contains(result1)
        assertThat(restaurant2.dishes).contains(result2)
    }

    @Test
    fun `updateDish should update dish successfully`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza()
        val updateRequest =
            DishUpdateRequest(
                name = "Updated Pizza",
                description = "Updated description",
                image = "updated-image.jpg",
            )
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId(updateRequest.name, restaurant.id)).willReturn(null)
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.updateDish(dishId, updateRequest)

        // Then
        assertThat(result.name).isEqualTo(updateRequest.name)
        assertThat(result.description).isEqualTo(updateRequest.description)
        assertThat(result.image).isEqualTo(updateRequest.image)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `updateDish should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        val updateRequest =
            DishUpdateRequest(
                name = "Updated Pizza",
                description = "Updated description",
                image = "updated-image.jpg",
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.updateDish(dishId, updateRequest)
        }
    }

    @Test
    fun `updateDish should throw DishAlreadyExistsException when name conflicts`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza()
        val existingDish = TestFixture.createMargheritaPizza().apply { id = 2L }
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        val updateRequest =
            DishUpdateRequest(
                name = "Existing Dish Name",
                description = "Updated description",
                image = "updated-image.jpg",
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId(updateRequest.name, restaurant.id)).willReturn(existingDish)

        // When & Then
        assertThrows<DishAlreadyExistsException> {
            dishService.updateDish(dishId, updateRequest)
        }
    }

    @Test
    fun `updateDish should allow same dish to keep its name`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        val updateRequest =
            DishUpdateRequest(
                name = dish.name,
                description = "Updated description",
                image = "updated-image.jpg",
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId(updateRequest.name, restaurant.id)).willReturn(dish) // Same dish
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.updateDish(dishId, updateRequest)

        // Then
        assertThat(result.name).isEqualTo(updateRequest.name)
        assertThat(result.description).isEqualTo(updateRequest.description)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `deleteDishById should delete dish successfully`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza()
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))

        // When
        dishService.deleteDishById(dishId)

        // Then
        verify(dishRepository).delete(dish)
    }

    @Test
    fun `deleteDishById should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.deleteDishById(dishId)
        }
    }

    @Test
    fun `patchDish should update only provided fields`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val originalName = dish.name
        val originalDescription = dish.description
        val patchRequest =
            DishPatchRequest(
                name = null,
                description = "Updated description only",
                image = null,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.patchDish(dishId, patchRequest)

        // Then
        assertThat(result.name).isEqualTo(originalName)
        assertThat(result.description).isNotEqualTo(originalDescription)
        assertThat(result.description).isEqualTo("Updated description only")
        verify(dishRepository).save(dish)
    }

    @Test
    fun `patchDish should update all provided fields`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        val patchRequest =
            DishPatchRequest(
                name = "New Name",
                description = "New Description",
                image = "new-image.jpg",
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId("New Name", restaurant.id)).willReturn(null)
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.patchDish(dishId, patchRequest)

        // Then
        assertThat(result.name).isEqualTo("New Name")
        assertThat(result.description).isEqualTo("New Description")
        assertThat(result.image).isEqualTo("new-image.jpg")
        verify(dishRepository).save(dish)
    }

    @Test
    fun `patchDish should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        val patchRequest = DishPatchRequest(name = "New Name")
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.patchDish(dishId, patchRequest)
        }
    }

    @Test
    fun `patchDish should throw DishAlreadyExistsException when name conflicts`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val existingDish = TestFixture.createMargheritaPizza().apply { id = 2L }
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        val patchRequest = DishPatchRequest(name = "Conflicting Name")
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId("Conflicting Name", restaurant.id)).willReturn(existingDish)

        // When & Then
        assertThrows<DishAlreadyExistsException> {
            dishService.patchDish(dishId, patchRequest)
        }
    }

    @Test
    fun `patchDish should allow same dish to keep its name`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val restaurant = TestFixture.createHappyBeansCafe().apply { 
            dishes.add(dish) 
        }
        val patchRequest = DishPatchRequest(name = dish.name) // Same name
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(restaurantRepository.findAll()).willReturn(listOf(restaurant))
        given(dishRepository.findByNameAndRestaurantId(dish.name, restaurant.id)).willReturn(dish) // Same dish
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.patchDish(dishId, patchRequest)

        // Then
        assertThat(result.name).isEqualTo(dish.name)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `patchDish should handle empty patch request`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val originalName = dish.name
        val originalDescription = dish.description
        val originalImage = dish.image
        val patchRequest = DishPatchRequest() // All fields null
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.patchDish(dishId, patchRequest)

        // Then - Nothing should change
        assertThat(result.name).isEqualTo(originalName)
        assertThat(result.description).isEqualTo(originalDescription)
        assertThat(result.image).isEqualTo(originalImage)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `addDishOption should add option to dish successfully`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val initialOptionsCount = dish.dishOptions.size
        val optionRequest =
            DishOptionCreateRequest(
                name = "Extra Large",
                description = "Perfect for big appetite",
                price = 29.99,
                image = "extra-large.jpg",
                prepTimeMinutes = 25,
                rating = 4.8,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.addDishOption(dishId, optionRequest)

        // Then
        assertThat(result.name).isEqualTo("Extra Large")
        assertThat(result.dish).isEqualTo(dish)
        assertThat(dish.dishOptions).hasSize(initialOptionsCount + 1)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `addDishOption should throw EntityNotFoundException when dish not found`() {
        // Given
        val dishId = 999L
        val optionRequest =
            DishOptionCreateRequest(
                name = "Extra Large",
                description = "Perfect for big appetite",
                price = 29.99,
                image = "extra-large.jpg",
                prepTimeMinutes = 25,
                rating = 4.8,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.addDishOption(dishId, optionRequest)
        }
    }

    @Test
    fun `updateDishOption should update option successfully`() {
        // Given
        val dishId = 1L
        val optionId = 1L
        val dish = TestFixture.createMargheritaPizzaWithAllOptions().apply { id = dishId }
        dish.dishOptions.first().apply { id = optionId }
        val updateRequest =
            DishOptionUpdateRequest(
                name = "Updated Option",
                description = "Updated description",
                price = 19.99,
                image = "updated-option.jpg",
                prepTimeMinutes = 20,
                rating = 4.9,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.updateDishOption(dishId, optionId, updateRequest)

        // Then
        assertThat(result.name).isEqualTo("Updated Option")
        assertThat(result.description).isEqualTo("Updated description")
        assertThat(result.price).isEqualTo(19.99)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `updateDishOption should throw EntityNotFoundException when option not found`() {
        // Given
        val dishId = 1L
        val optionId = 999L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        val updateRequest =
            DishOptionUpdateRequest(
                name = "Updated Option",
                description = "Updated description",
                price = 19.99,
                image = "updated-option.jpg",
                prepTimeMinutes = 20,
                rating = 4.9,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.updateDishOption(dishId, optionId, updateRequest)
        }
    }

    @Test
    fun `patchDishOption should update only provided fields`() {
        // Given
        val dishId = 1L
        val optionId = 1L
        val dish = TestFixture.createMargheritaPizzaWithAllOptions().apply { id = dishId }
        val dishOption = dish.dishOptions.first().apply { id = optionId }
        val originalName = dishOption.name
        val originalImage = dishOption.image
        val patchRequest =
            DishOptionPatchRequest(
                name = null,
                description = "Patched description",
                price = 15.99,
                image = null,
                prepTimeMinutes = null,
                rating = null,
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.patchDishOption(dishId, optionId, patchRequest)

        // Then
        assertThat(result.name).isEqualTo(originalName)
        assertThat(result.description).isEqualTo("Patched description")
        assertThat(result.price).isEqualTo(15.99)
        assertThat(result.image).isEqualTo(originalImage)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `deleteDishOption should remove option from dish successfully`() {
        // Given
        val dishId = 1L
        val optionId = 1L
        val dish = TestFixture.createMargheritaPizzaWithAllOptions().apply { id = dishId }
        val initialOptionsCount = dish.dishOptions.size
        val dishOption = dish.dishOptions.first().apply { id = optionId }
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        dishService.deleteDishOption(dishId, optionId)

        // Then
        assertThat(dish.dishOptions).hasSize(initialOptionsCount - 1)
        assertThat(dish.dishOptions).doesNotContain(dishOption)
        verify(dishRepository).save(dish)
    }

    @Test
    fun `deleteDishOption should throw EntityNotFoundException when option not found`() {
        // Given
        val dishId = 1L
        val optionId = 999L
        val dish = TestFixture.createMargheritaPizza().apply { id = dishId }
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.deleteDishOption(dishId, optionId)
        }
    }
}
