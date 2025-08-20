package happybeans.service

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.enums.TagContainerType
import happybeans.enums.UserRole
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.Restaurant
import happybeans.model.RestaurantReview
import happybeans.model.Tag
import happybeans.model.TagContainer
import happybeans.model.User
import happybeans.model.WorkingDateHour
import happybeans.repository.DishOptionRepository
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.RestaurantReviewRepository
import happybeans.repository.TagContainerRepository
import happybeans.repository.TagRepository
import happybeans.repository.UserRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.DayOfWeek
import java.time.LocalTime
import javax.management.relation.Role

@SpringBootTest
@Transactional
class RestaurantReviewServiceTest {
    @Autowired
    private lateinit var restaurantReviewService: RestaurantReviewService

    @Autowired
    private lateinit var restaurantReviewRepository: RestaurantReviewRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var dishOptionRepository: DishOptionRepository

    @Autowired
    private lateinit var tagContainerRepository: TagContainerRepository

    @Autowired
    private lateinit var tagRepository: TagRepository

    @Autowired
    private lateinit var dishRepository: DishRepository

    private lateinit var member: User
    private lateinit var owner: User
    private lateinit var restaurant: Restaurant
    private lateinit var dishOption: DishOption
    private lateinit var dish: Dish

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

        owner = userRepository.save(
            User(
                email = "owner-login@test.com",
                password = "12345678",
                firstName = "Test",
                lastName = "User",
                role = UserRole.RESTAURANT_OWNER,
            ),
        )

        restaurant =
            Restaurant(
                user = owner,
                name = "Cozy Bistro",
                description = "A charming bistro with a warm atmosphere",
                image = "https://example.com/cozy-bistro.jpg",
                addressUrl = "https://maps.example.com/cozy-bistro",
                workingDateHours =
                    mutableListOf(
                        WorkingDateHour(
                            dayOfWeek = DayOfWeek.MONDAY,
                            openTime = LocalTime.of(9, 0),
                            closeTime = LocalTime.of(22, 0),
                        ),
                        WorkingDateHour(
                            dayOfWeek = DayOfWeek.TUESDAY,
                            openTime = LocalTime.of(9, 0),
                            closeTime = LocalTime.of(22, 0),
                        ),
                    ),
            )
        restaurant = restaurantRepository.save(restaurant)

        dish =
            Dish(
                name = "Pasta Primavera",
                description = "Fresh pasta with seasonal vegetables",
                image = "https://example.com/pasta-primavera.jpg",
            )
        restaurant.addDish(dish)
        dish = dishRepository.save(dish)

        val tag = Tag(name = "Tomato")
        val savedTag = tagRepository.save(tag)

        val tagContainer =
            TagContainer(
                tags = mutableListOf(savedTag),
                type = TagContainerType.INGREDIENTS,
                user = null,
                dish = dish,
            )
        val savedTagContainer = tagContainerRepository.save(tagContainer)

        dishOption =
            DishOption(
                dish = dish,
                name = "Regular Portion",
                description = "Standard serving size",
                price = 12.99,
                image = "https://example.com/regular-portion.jpg",
                available = true,
                rating = 4.0,
                prepTimeMinutes = 15,
            )
        dish.dishOptions.add(dishOption)
        dishOption = dishOptionRepository.save(dishOption)
    }

    @Test
    fun `create restaurant review with valid Id`() {
        val request =
            ReviewCreateRequestDto(
                rating = 4.0,
                message = "cozy ambience and polite staff",
                entityId = restaurant.id,
            )

        val response = restaurantReviewService.createRestaurantReview(member, request)
        assertThat(response).isNotNull
        assertThat(response).isGreaterThan(0)
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

        assertThrows<IllegalArgumentException> {
            restaurantReviewService.createRestaurantReview(member, request)
        }
    }

    @Test
    fun `get all reviews returns correct number of reviews`() {
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 1",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 2",
                restaurantId = restaurant.id,
            ),
        )

        val reviews = restaurantReviewService.getAllReviews()
        assertThat(reviews).hasSize(2)
    }

    @Test
    fun `get reviews by restaurantId returns correct reviews`() {
        val restaurant2 =
            restaurantRepository.save(
                Restaurant(
                    user = owner,
                    name = "Cozy Cafe",
                    description = "A charming bistro with a warm atmosphere",
                    image = "https://example.com/cozy-cafe.jpg",
                    addressUrl = "https://maps.example.com/cozy-cafe",
                    workingDateHours =
                        mutableListOf(
                            WorkingDateHour(
                                dayOfWeek = DayOfWeek.MONDAY,
                                openTime = LocalTime.of(9, 0),
                                closeTime = LocalTime.of(22, 0),
                            ),
                            WorkingDateHour(
                                dayOfWeek = DayOfWeek.SUNDAY,
                                openTime = LocalTime.of(9, 0),
                                closeTime = LocalTime.of(17, 0),
                            ),
                        ),
                ),
            )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 1",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 2",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 3.0,
                message = "Review on other restaurant",
                restaurantId = restaurant2.id,
            ),
        )

        val reviews = restaurantReviewService.getReviewsByRestaurantId(restaurant.id)
        assertThat(reviews).hasSize(2)
        reviews.forEach {
            assertThat(it.restaurantId).isEqualTo(restaurant.id)
        }
    }

    @Test
    fun `get reviews by user ID returns correct reviews`() {
        val otherMember =
            userRepository.save(
                User(
                    email = "other@test.com",
                    password = "pass",
                    firstName = "Other",
                    lastName = "User",
                ),
            )

        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 1",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 2",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = otherMember.id,
                userName = "Other User",
                rating = 3.0,
                message = "Review from other user",
                restaurantId = restaurant.id,
            ),
        )

        val reviews = restaurantReviewService.getReviewsByUserId(member.id)
        assertThat(reviews).hasSize(2)
    }

    @Test
    fun `get average rating for restaurant returns correct average`() {
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 5.0,
                message = "Review 1",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 3.0,
                message = "Review 2",
                restaurantId = restaurant.id,
            ),
        )
        restaurantReviewRepository.save(
            RestaurantReview(
                userId = member.id,
                userName = "Test User",
                rating = 4.0,
                message = "Review 3",
                restaurantId = restaurant.id,
            ),
        )

        val averageRating = restaurantReviewService.getAverageRatingForRestaurant(restaurant.id)
        assertThat(averageRating).isEqualTo(4.0)
    }

    @Test
    fun `get average rating for restaurant with no reviews returns 0`() {
        val averageRating = restaurantReviewService.getAverageRatingForRestaurant(restaurant.id)
        assertThat(averageRating).isEqualTo(0.0)
    }
}
