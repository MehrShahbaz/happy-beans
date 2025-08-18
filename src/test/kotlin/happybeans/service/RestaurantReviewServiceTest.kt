package happybeans.service

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.RestaurantReview
import happybeans.model.User
import happybeans.repository.RestaurantReviewRepository
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
class RestaurantReviewServiceTest {
    @Autowired
    private lateinit var restaurantReviewService: RestaurantReviewService

    @Autowired
    private lateinit var restaurantReviewRepository: RestaurantReviewRepository

    @Autowired
    private lateinit var userRepository: UserRepository
//
//    @Autowired
//    private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var member: User
//    private lateinit var restaurant: Restaurant

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
        //        restaurant = restaurantRepository.save(
//            Restaurant(
//                name = "Cozy Bistro",
//                address = "123 Main St"
//            )
//        )
    }

    @Test
    fun `create restaurant review with valid Id`() {
        val request =
            ReviewCreateRequestDto(
                rating = 4.0,
                message = "cozy ambience and polite staff",
                entityId = 1L,
                // restaurant.id,
            )

        val response = restaurantReviewService.createRestaurantReview(member, request)
        assertThat(response).isNotNull
        assertThat(response.id).isGreaterThan(0)
        assertThat(response.rating).isEqualTo(4.0)
        assertThat(response.message).contains("cozy ambience and polite staff")
//        assertThat(response.entityId).isEqualTo(restaurant.id)
    }

    @Test
    fun `throw EntityNotFoundException for restaurant review with invalid restaurant ID`() {
        val invalidRestaurantId = 10000L
        val request =
            ReviewCreateRequestDto(
                rating = 4.0,
                message = "cozy ambience and polite staff",
                entityId = invalidRestaurantId,
            )

        assertThrows<EntityNotFoundException> {
            restaurantReviewService.createRestaurantReview(member, request)
        }
    }

    @Test
    fun `update restaurant review message`() {
        val review =
            restaurantReviewRepository.save(
                RestaurantReview(
                    userId = member.id,
                    userName = "Test User",
                    rating = 3.0,
                    message = "Original message",
                    restaurantId = 1L,
                    // restaurant.id
                ),
            )
        val updateDto = ReviewUpdateRequestDto(message = "Updated message")

        val response = restaurantReviewService.updateRestaurantReview(review.id, updateDto, member)

        assertThat(response).isNotNull
        assertThat(response.id).isEqualTo(review.id)
        assertThat(response.message).isEqualTo("Updated message")
        assertThat(response.updatedAt).isNotNull()
    }

    @Test
    fun `get all reviews returns correct number of reviews`() {
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 1",
                restaurantId = 1L,
                // restaurant.id
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 2",
                restaurantId = 1L,
                // restaurant.id
            ),
        )

        val reviews = restaurantReviewService.getAllReviews()
        assertThat(reviews).hasSize(2)
    }

    @Test
    fun `get reviews by restaurantId returns correct reviews`() {
//            val otherRestaurant = restaurantRepository.save(Restaurant(name = "Rise and Shine Berlin", address = "Oranienburger Str. 27"))

        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 1",
                restaurantId = 2L,
                // restaurant.id
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 2",
                restaurantId = 2L,
                // restaurant.id
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 3.0,
                message = "Review on other restaurant",
                restaurantId = 2L,
                // otherRestaurant.id
            ),
        )

//            val reviews = restaurantReviewService.getReviewsByRestaurantId(restaurant.id)
//            assertThat(reviews).hasSize(2)
//            reviews.forEach {
//                assertThat(it.restaurantId).isEqualTo(restaurant.id)
//            }
    }

    @Test
    fun `get reviews by user ID returns correct reviews`() {
        val otherUser =
            userRepository.save(
                User(
                    email = "other@test.com",
                    password = "pass",
                    firstName = "Other",
                    lastName = "User",
                ),
            )

//            restaurantReviewRepository.save(RestaurantReview(userId = user.id, userName = "Test User", rating = 4.0, message = "Review 1", restaurantId = restaurant.id))
//            restaurantReviewRepository.save(RestaurantReview(userId = user.id, userName = "Test User", rating = 5.0, message = "Review 2", restaurantId = restaurant.id))
//            restaurantReviewRepository.save(RestaurantReview(userId = otherUser.id, userName = "Other User", rating = 3.0, message = "Review from other user", restaurantId = restaurant.id))

        val reviews = restaurantReviewService.getReviewsByUserId(member.id)
        assertThat(reviews).hasSize(2)
    }

//        @Test
//        fun `get average rating for restaurant returns correct average`() {
//            restaurantReviewRepository.save(RestaurantReview(userId = user.id, userName = "Test User", rating = 5.0, message = "Review 1", restaurantId = restaurant.id))
//            restaurantReviewRepository.save(RestaurantReview(userId = user.id, userName = "Test User", rating = 3.0, message = "Review 2", restaurantId = restaurant.id))
//            restaurantReviewRepository.save(RestaurantReview(userId = user.id, userName = "Test User", rating = 4.0, message = "Review 3", restaurantId = restaurant.id))
//
//            val averageRating = restaurantReviewService.getAverageRatingForRestaurant(restaurant.id)
//            assertThat(averageRating).isEqualTo(4.0)
//        }
//
//        @Test
//        fun `get average rating for restaurant with no reviews returns 0`() {
//            val averageRating = restaurantReviewService.getAverageRatingForRestaurant(restaurant.id)
//            assertThat(averageRating).isEqualTo(0.0)
//        }
}
