package happybeans.service

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.model.DishReview
import happybeans.model.User
import happybeans.repository.DishReviewRepository
import happybeans.repository.UserRepository
import happybeans.utils.exception.EntityNotFoundException
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class DishReviewServiceTest {
    @Autowired
    private lateinit var dishReviewService: DishReviewService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var dishReviewRepository: DishReviewRepository

//        @Autowired
//        private lateinit var dishOptionRepository: DishOptionRepository

    private lateinit var member: User
//        private lateinit var dishOption: DishOption

    @BeforeEach
    fun setUp() {
        member =
            userRepository.save(
                User(
                    email = "user-login@test.com",
                    password = "12345678",
                    firstName = "Test",
                    lastName = "User",
                ),
            )
//
//            dishOption = dishOptionRepository.save(
//                DishOption(
//                    name = "Pasta Carbonara",
//                    price = 12.99,
//                    restaurantId = 1L
//                )
//            )
    }

    @Test
    fun `create dish review with valid IDs`() {
        val request =
            ReviewCreateRequestDto(
                rating = 3.0,
                message = "perfectly cooked pasta",
                entityId = 1L,
                // dishOption.id
            )

        val response = dishReviewService.createDishReview(member, request)

        assertThat(response).isNotNull
        assertThat(response.id).isGreaterThan(0)
        assertThat(response.rating).isEqualTo(3.0)
        assertThat(response.message).isEqualTo("perfectly cooked pasta")
//            assertThat(response.dishOptionId).isEqualTo(dishOption.id)
    }

    @Test
    fun `throw EntityNotFoundException for dish review with invalid dish option ID`() {
        val invalidDishOptionId = 1000L
        val request =
            ReviewCreateRequestDto(
                rating = 3.0,
                message = "perfectly cooked pasta",
                entityId = invalidDishOptionId,
            )

        assertThrows<EntityNotFoundException> {
            dishReviewService.createDishReview(member, request)
        }
    }

    @Test
    fun `throw EntityNotFoundException for dish review with invalid user ID`() {
        val invalidUserId = 1000L
        val request =
            ReviewCreateRequestDto(
                rating = 3.0,
                message = "perfectly cooked pasta",
                entityId = 1L,
                // dishOption.id
            )

        assertThrows<EntityNotFoundException> {
            dishReviewService.createDishReview(member, request)
        }
    }

    @Test
    fun `get all reviews returns correct number of reviews`() {
        dishReviewRepository.save(
            DishReview(
                userId = member.id,
                userName = member.firstName,
                rating = 4.0,
                message = "great",
                dishOptionId = 1L,
                // dishOption.id
            ),
        )
        dishReviewRepository.save(
            DishReview(
                userId = member.id,
                userName = member.firstName,
                rating = 5.0,
                message = "amazing",
                dishOptionId = 2L,
                // dishOption.id
            ),
        )

        val reviews = dishReviewService.getAllReviews()
        assertThat(reviews).hasSize(2)
    }

//    @Test
//    fun `get average rating for dish option returns correct average`() {
//        dishReviewRepository.save(
//            DishReview(
//                userId = member.id,
//                userName = member.firstName,
//                rating = 5.0,
//                message = "tasty",
//                dishOptionId = 1L//dishOption.id
//            )
//        )
//        dishReviewRepository.save(
//            DishReview(
//                userId = member.id,
//                userName = member.firstName,
//                rating = 3.0,
//                message = "okay",
//                dishOptionId = 1L//dishOption.id
//            )
//        )
//        dishReviewRepository.save(
//            DishReview(
//                userId = member.id,
//                userName = member.firstName,
//                rating = 4.0,
//                message = "good",
//                dishOptionId = 2L//dishOption.id
//            )
//        )
//
//        val averageRating = dishReviewService.getAverageRatingForDishOption(dishOption.id)
//        assertThat(averageRating).isEqualTo(4.0)
//    }
//
//    @Test
//    fun `get average rating for dish option with no reviews returns 0`() {
//        val averageRating = dishReviewService.getAverageRatingForDishOption(dishOption.id)
//        assertThat(averageRating).isEqualTo(0.0)
//    }
}
