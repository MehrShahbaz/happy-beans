package happybeans.service

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.model.User
import happybeans.repository.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class ReviewServiceTest {
    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var userRepository: UserRepository

//    @Autowired
//    private lateinit var dishRepository: DishRepository

    //    @Autowired
//    private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var user: User
//    private lateinit var dish: Dish
//    private lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user = userRepository.save(
            User(
                email = "user-login@test.com",
                password = "12345678",
                firstName = "Test",
                lastName = "User"
            )
        )
//        dish = dishRepository.save(
//            Dish(
//                name = "Pasta Carbonara",
//                price = 12.99,
//                restaurantId = 1L
//            )
//        )
//        restaurant = restaurantRepository.save(
//            Restaurant(
//                name = "Cozy Bistro",
//                address = "123 Main St"
//            )
//        )
    }

    @Test
    fun `create dish review with valid Id`() {
        val request = ReviewCreateRequestDto(
            userId = user.id,
            rating = 3,
            message = "perfectly cooked pasta",
            dishId = 1,
            restaurantId = null,
        )

        val response = reviewService.createDishReview(request)
        assertThat(response).isNotNull
        assertThat(response.message).contains("perfectly cooked")
    }

//    @Test
//    fun `throw EntityNotFoundException for dish review with invalid Id`() {
//        val request = ReviewCreateRequestDto(
//            userId = user.id,
//            rating = 3,
//            message = "perfectly cooked pasta",
//            dishId = 1000,
//            restaurantId = null,
//        )
//
//        assertThrows<EntityNotFoundException> {
//            reviewService.createDishReview(request)
//            }
//    }

    @Test
    fun `create restaurant review with valid Id`() {
        val request = ReviewCreateRequestDto(
            userId = user.id,
            rating = 4,
            message = "cozy ambience and polite staff",
            dishId = null,
            restaurantId = 1L,
        )

        val response = reviewService.createRestaurantReview(request)
        assertThat(response).isNotNull
        assertThat(response.message).contains("cozy ambience")
    }

//    @Test
//    fun `throw EntityNotFoundException for restaurant review with invalid Id`() {
//        val user =
//            userRepository.save(
//                User(
//                    "user-login@test.com",
//                    "12345678",
//                    "Test",
//                    "User",
//                ),
//            )
//        val request = ReviewCreateRequestDto(
//            userId = user.id,
//            rating = 4,
//            message = "cozy ambience and polite staff",
//            dishId = null,
//            restaurantId = 10000L,
//        )
//
//       assertThrows<EntityNotFoundException> {
//           reviewService.createRestaurantReview(request)
//       }
//    }

    @Test
    fun `throw IllegalArgumentException for restaurant review with dish Id`() {
        val user =
            userRepository.save(
                User(
                    "user-login@test.com",
                    "12345678",
                    "Test",
                    "User",
                ),
            )
        val request = ReviewCreateRequestDto(
            userId = user.id,
            rating = 4,
            message = "cozy ambience and polite staff",
            dishId = 1L,
            restaurantId = 1L,
        )

        assertThrows<IllegalArgumentException> {
           reviewService.createRestaurantReview(request)
       }
    }

    @Test
    fun `throw IllegalArgumentException for dish review with restaurant Id`() {
        val user =
            userRepository.save(
                User(
                    "user-login@test.com",
                    "12345678",
                    "Test",
                    "User",
                ),
            )
        val request = ReviewCreateRequestDto(
            userId = user.id,
            rating = 4,
            message = "cozy ambience and polite staff",
            dishId = 1L,
            restaurantId = 1L,
        )

        assertThrows<IllegalArgumentException> {
            reviewService.createDishReview(request)
        }
    }
}