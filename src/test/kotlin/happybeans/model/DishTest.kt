package happybeans

import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.TagContainerRepository
import happybeans.service.DishService
import happybeans.utils.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.findByIdOrNull

@ExtendWith(MockitoExtension::class)
class DishTest {
    @Mock
    private lateinit var dishRepository: DishRepository

    @Mock
    private lateinit var restaurantRepository: RestaurantRepository

    @Mock
    private lateinit var tagContainerRepository: TagContainerRepository

    @InjectMocks
    private lateinit var dishService: DishService

    @Test
    fun `findById should return MargheritaPizza without option if exists`() {
        val dishFromFixture = TestFixture.createMargheritaPizza()
        given(dishRepository.findByIdOrNull(1L)).willReturn(dishFromFixture)

        val result = dishService.findById(1L)
        assertThat(result.name).isEqualTo(dishFromFixture.name)
    }

    @Test
    fun `findById should return MargheritaPizza with all options if exists`() {
        val dishFromFixture = TestFixture.createMargheritaPizzaWithAllOptions()
        given(dishRepository.findByIdOrNull(1L)).willReturn(dishFromFixture)

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
        given(dishRepository.findByIdOrNull(999L)).willReturn(null)

        // When && Then
        assertThrows<EntityNotFoundException> {
            dishService.findById(999L)
        }
    }

    @Test
    fun `findByRestaurantId should return list of dishes for valid restaurant`() {
        // Given
        val restaurantId = 1L
        val pageable = PageRequest.of(0, 10)
        val dishes =
            listOf(
                TestFixture.createMargheritaPizza(),
                TestFixture.createClassicBeefBurger(),
                TestFixture.createCaesarSalad(),
            )

        given(dishRepository.findByRestaurantId(restaurantId, pageable)).willReturn(dishes)

        val result = dishService.findByRestaurantId(restaurantId, pageable)

        assertThat(result).hasSize(3)
        assertThat(result.map { it.name }).containsExactlyInAnyOrder(
            "Margherita Pizza",
            "Classic Beef Burger",
            "Caesar Salad",
        )
    }

    @Test
    fun `findByName should return dish when name matches`() {
        // Given
        val dishName = "Margherita Pizza"
        val dish = TestFixture.createMargheritaPizza()
        given(dishRepository.findByNameOrNull(dishName)).willReturn(dish)

        // When
        val result = dishService.findByName(dishName)

        // Then
        assertThat(result).isNotNull
    }

    @Test
    fun `findByRestaurantId should return empty list when restaurant has no dishes`() {
        // Given
        val restaurantId = 1L
        val pageable = PageRequest.of(0, 10)

        given(dishRepository.findByRestaurantId(restaurantId, pageable)).willReturn(emptyList())

        val result = dishService.findByRestaurantId(restaurantId, pageable)

        assertThat(result).isEmpty()
    }

    @Test
    fun `findByName should return null when dish not found`() {
        // Given
        val dishName = "Non-existent Dish"
        given(dishRepository.findByNameOrNull(dishName)).willReturn(null)

        // When
        val result = dishService.findByName(dishName)

        // Then
        assertThat(result).isNull()
    }
}
