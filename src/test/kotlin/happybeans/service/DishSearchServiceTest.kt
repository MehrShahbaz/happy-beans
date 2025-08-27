package happybeans.service

import happybeans.TestFixture
import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishUpdateRequest
import happybeans.enums.UserRole
import happybeans.model.Dish
import happybeans.model.Restaurant
import happybeans.model.Tag
import happybeans.model.User
import happybeans.repository.DishOptionRepository
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.UserRepository
import happybeans.utils.exception.DishAlreadyExistsException
import happybeans.utils.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.eq
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import java.util.Optional

@SpringBootTest
@Transactional
class DishSearchServiceTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dishRepository: DishRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var dishService: DishService

    @Autowired
    private lateinit var userService: UserService

    @BeforeEach
    fun setUp() {
//        val testUser = userRepository.save(
//            User(
//                email = "user@user.com",
//                password = "password",
//                firstName = "Test",
//                lastName = "User",
//                role = UserRole.USER,
//            )
//        )

        val testRestaurant = TestFixture.createHappyBeansCafe()
        restaurantRepository.save(testRestaurant)
        val testOwner = userRepository.save(testRestaurant.user)

        val dish = TestFixture.createPizzaWithAllOptions()
        dishRepository.save(dish)

        userService.addUserLike(
            1L,
            "spicy"
        )
        userService.addUserDislike(
            1L,
            "bitter"
        )
        dishService.addDishOptionTag(
            1L,
            "spicy",
            testOwner
        )
        dishService.addDishOptionTag(
            2L,
            "sweet",
            testOwner
        )
        dishService.addDishOptionTag(
            3L,
            "bitter",
            testOwner
        )
    }

    @Test
    fun `getFilteredDishOptionsByUser should show filtered options successfully`() {
        // given
        assertThat(dishRepository.findById(1L)).isNotEmpty



    }
}