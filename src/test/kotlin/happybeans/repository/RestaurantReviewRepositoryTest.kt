package happybeans.repository

import happybeans.model.RestaurantReview
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class RestaurantReviewRepositoryTest {
    @Autowired
    private lateinit var restaurantReviewRepository: RestaurantReviewRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `findAverageRestaurantRating should return correct average rating for restaurant`() {
        val restaurantId = 1L
        val reviews =
            listOf(
                RestaurantReview(
                    userId = 1L,
                    userName = "User1",
                    rating = 4.5,
                    message = "Test",
                    restaurantId = restaurantId,
                    restaurantName = "Restaurant1",
                ),
                RestaurantReview(
                    userId = 1L,
                    userName = "User1",
                    rating = 3.5,
                    message = "Test",
                    restaurantId = restaurantId,
                    restaurantName = "Restaurant1",
                ),
                RestaurantReview(
                    userId = 1L,
                    userName = "User1",
                    rating = 5.0,
                    message = "Test",
                    restaurantId = restaurantId,
                    restaurantName = "Restaurant1",
                ),
            )
        reviews.forEach { entityManager.persist(it) }
        entityManager.flush()
        val averageRating = restaurantReviewRepository.findAverageRestaurantRating(restaurantId)
        assertEquals(4.333333333333333, averageRating!!, 0.0001, "Average rating should be approximately 4.33")
    }

    @Test
    fun `findAverageDishOptionRating should return null when no reviews exist for dish option`() {
        // Given
        val restaurantId = 2L

        // When
        val averageRating = restaurantReviewRepository.findAverageRestaurantRating(restaurantId)

        // Then
        assertNull(averageRating, "Average rating should be null when no reviews exist")
    }

    @Test
    fun `findAverageDishOptionRating should return correct average for single review`() {
        // Given
        val restaurantId = 3L
        val review =
            RestaurantReview(
                userId = 1L,
                userName = "User1",
                rating = 5.0,
                message = "Test",
                restaurantId = restaurantId,
                restaurantName = "Restaurant1",
            )

        entityManager.persist(review)
        entityManager.flush()

        // When
        val averageRating = restaurantReviewRepository.findAverageRestaurantRating(restaurantId)

        // Then
        assertEquals(5.0, averageRating!!, 0.0001, "Average rating should be 4.0 for a single review")
    }
}
