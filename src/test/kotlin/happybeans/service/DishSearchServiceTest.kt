package happybeans.service

import happybeans.TestFixture
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

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
        dishRepository.deleteAll()
        restaurantRepository.deleteAll()
        userRepository.deleteAll()

        val testUser =
            userRepository.save(
                User(
                    email = "user@user.com",
                    password = "password",
                    firstName = "Test",
                    lastName = "User",
                    role = UserRole.USER,
                ),
            )

        val testRestaurant = TestFixture.createHappyBeansCafe()
        val savedRestaurant = restaurantRepository.save(testRestaurant)
        val testOwner = userRepository.save(testRestaurant.user)

        val dish = TestFixture.createPizzaWithAllOptions()
        savedRestaurant.addDish(dish)
        val savedDish = dishRepository.save(dish)

        userService.addUserLike(
            testUser.id,
            "spicy",
        )
        userService.addUserLike(
            testUser.id,
            "meat",
        )
        userService.addUserLike(
            testUser.id,
            "sweet",
        )

        userService.addUserDislike(
            testUser.id,
            "bitter",
        )

        val dishOptions = savedDish.dishOptions.toList().sortedBy { it.name }

        dishService.addDishOptionTag(
            dishOptions.find { it.name.contains("Bitter") }!!.id,
            "bitter",
            testOwner,
        )
        dishService.addDishOptionTag(
            dishOptions.find { it.name.contains("Spicy") }!!.id,
            "spicy",
            testOwner,
        )
        dishService.addDishOptionTag(
            dishOptions.find { it.name.contains("Spicy") }!!.id,
            "meat",
            testOwner,
        )
        dishService.addDishOptionTag(
            dishOptions.find { it.name.contains("Sweet") }!!.id,
            "sweet",
            testOwner,
        )
    }

    @Test
    fun `getFilteredDishOptionsByUserTags should show filtered options successfully`() {
        // Given
        val testUser = userRepository.findByEmail("user@user.com").get()
        val likes = userService.getUserLikes(testUser.id).map { it.name }.toSet()
        val dislikes = userService.getUserDislikes(testUser.id).map { it.name }.toSet()

        // When
        val filteredOptions = dishService.getFilteredDishOptionsByUserTags(likes, dislikes)

        // Then : dislikedTag(bitter) is filtered out
        assertThat(filteredOptions).hasSize(2)

        // Given: options are present
        val optionNames = filteredOptions.map { it.name }
        assertThat(optionNames).contains("Spicy Margherita (8\")")
        assertThat(optionNames).contains("Sweet Margherita (8\")")
        assertThat(optionNames).doesNotContain("Bitter Margherita (8\")")

        // Then: First option should be spicy (matches 2 liked tags: spicy + meat)
        assertThat(filteredOptions[0].name).isEqualTo("Spicy Margherita (8\")")
        // Verify the tags through the service since dishOptionTags is now JsonIgnore
        val spicyOptionTags = dishService.getDishOptionTags(filteredOptions[0].id)
        assertThat(spicyOptionTags.any { it.name == "spicy" }).isTrue()
        assertThat(spicyOptionTags.any { it.name == "meat" }).isTrue()

        // Then: Second option should be sweet (matches 1 liked tag: sweet)
        assertThat(filteredOptions[1].name).isEqualTo("Sweet Margherita (8\")")
        val sweetOptionTags = dishService.getDishOptionTags(filteredOptions[1].id)
        assertThat(sweetOptionTags.any { it.name == "sweet" }).isTrue()

        // Then: Verify bitter option is not present by checking no filtered options have bitter tag
        val allTagsFromFilteredOptions =
            filteredOptions.flatMap {
                dishService.getDishOptionTags(it.id).map { tag -> tag.name }
            }
        assertThat(allTagsFromFilteredOptions).doesNotContain("bitter")
    }
}
