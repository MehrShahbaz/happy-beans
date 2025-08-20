package happybeans

import happybeans.dto.dish.DishUpdateRequest
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.service.DishService
import happybeans.utils.exception.DishAlreadyExistsException
import happybeans.utils.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class DishTest {
    @Mock
    private lateinit var dishRepository: DishRepository

    @Mock
    private lateinit var restaurantRepository: RestaurantRepository

    @InjectMocks
    private lateinit var dishService: DishService

    @Test
    fun `findById should return MargheritaPizza without option if exists`() {
        val dishFromFixture = TestFixture.createMargheritaPizza()
        given(dishRepository.findById(1L)).willReturn(Optional.of(dishFromFixture))

        val result = dishService.findById(1L)
        assertThat(result.name).isEqualTo(dishFromFixture.name)
    }

    @Test
    fun `findById should return MargheritaPizza with all options if exists`() {
        val dishFromFixture = TestFixture.createMargheritaPizzaWithAllOptions()
        given(dishRepository.findById(1L)).willReturn(Optional.of(dishFromFixture))

        val result = dishService.findById(1L)

        assertThat(result.name).isEqualTo(dishFromFixture.name)
        assertThat(result.dishOptions).isNotEmpty
        assertThat(result.dishOptions).hasSize(dishFromFixture.dishOptions.size)

        val foundOptionNames = result.dishOptions.map { it.name }.toSet()
        val expectedOptionNames = dishFromFixture.dishOptions.map { it.name }.toSet()
        assertThat(foundOptionNames).isEqualTo(expectedOptionNames)

        val anyOption = result.dishOptions.first()
        assertThat(anyOption.dish).isEqualTo(result)
    }

    @Test
    fun `findById should throw NotFoundException when dish does not exist`() {
        // Given
        given(dishRepository.findById(999L)).willReturn(Optional.empty())

        // When && Then
        assertThrows<EntityNotFoundException> {
            dishService.findById(999L)
        }
    }

    @Test
    fun `findByName should return dish when name matches`() {
        // Given
        val dishName = "Margherita Pizza"
        val dish = TestFixture.createMargheritaPizza()
        given(dishRepository.findByName(dishName)).willReturn(dish)

        // When
        val result = dishService.findByName(dishName)

        // Then
        assertThat(result).isNotNull
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
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.findByName(updateRequest.name)).willReturn(null)
        given(dishRepository.save(dish)).willReturn(dish)

        // When
        val result = dishService.updateDish(dishId, updateRequest)

        // Then
        assertThat(result.name).isEqualTo(updateRequest.name)
        assertThat(result.description).isEqualTo(updateRequest.description)
        assertThat(result.image).isEqualTo(updateRequest.image)
    }

    @Test
    fun `updateDish should throw DishAlreadyExistsException when name conflicts`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza()
        val existingDish = TestFixture.createMargheritaPizza().apply { id = 2L }
        val updateRequest =
            DishUpdateRequest(
                name = "Existing Dish Name",
                description = "Updated description",
                image = "updated-image.jpg",
            )
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))
        given(dishRepository.findByName(updateRequest.name)).willReturn(existingDish)

        // When & Then
        assertThrows<DishAlreadyExistsException> {
            dishService.updateDish(dishId, updateRequest)
        }
    }

    @Test
    fun `deleteDishById should delete dish successfully`() {
        // Given
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizza()
        given(dishRepository.findById(dishId)).willReturn(Optional.of(dish))

        // When
        dishService.deleteDishById(dishId)

        // Then - no exception thrown
    }

    @Test
    fun `deleteDishById should throw EntityNotFoundException when dish does not exist`() {
        // Given
        val dishId = 999L
        given(dishRepository.findById(dishId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            dishService.deleteDishById(dishId)
        }
    }
}
