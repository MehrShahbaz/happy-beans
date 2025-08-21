package happybeans.repository

import happybeans.model.DishReview
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager

@DataJpaTest
class DishReviewRepositoryTest {
    @Autowired
    private lateinit var dishReviewRepository: DishReviewRepository

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Test
    fun `findAverageDishOptionRating should return correct average rating for dish option`() {
        // Given
        val dishOptionId = 1L
        val reviews =
            listOf(
                DishReview(
                    userId = 1L,
                    userName = "User1",
                    rating = 4.5,
                    message = "Great dish!",
                    dishOptionId = dishOptionId,
                    dishOptionName = "Option1",
                    dishOptionPrice = 10.0,
                ),
                DishReview(
                    userId = 2L,
                    userName = "User2",
                    rating = 3.5,
                    message = "Good dish!",
                    dishOptionId = dishOptionId,
                    dishOptionName = "Option1",
                    dishOptionPrice = 10.0,
                ),
                DishReview(
                    userId = 3L,
                    userName = "User3",
                    rating = 5.0,
                    message = "Amazing dish!",
                    dishOptionId = dishOptionId,
                    dishOptionName = "Option1",
                    dishOptionPrice = 10.0,
                ),
            )

        reviews.forEach { entityManager.persist(it) }
        entityManager.flush()

        // When
        val averageRating = dishReviewRepository.findAverageDishOptionRating(dishOptionId)

        // Then
        assertEquals(4.333333333333333, averageRating!!, 0.0001, "Average rating should be approximately 4.33")
    }

    @Test
    fun `findAverageDishOptionRating should return null when no reviews exist for dish option`() {
        // Given
        val dishOptionId = 2L

        // When
        val averageRating = dishReviewRepository.findAverageDishOptionRating(dishOptionId)

        // Then
        assertNull(averageRating, "Average rating should be null when no reviews exist")
    }

    @Test
    fun `findAverageDishOptionRating should return correct average for single review`() {
        // Given
        val dishOptionId = 3L
        val review =
            DishReview(
                userId = 1L,
                userName = "User1",
                rating = 4.0,
                message = "Single review!",
                dishOptionId = dishOptionId,
                dishOptionName = "Option3",
                dishOptionPrice = 12.0,
            )

        entityManager.persist(review)
        entityManager.flush()

        // When
        val averageRating = dishReviewRepository.findAverageDishOptionRating(dishOptionId)

        // Then
        assertEquals(4.0, averageRating!!, 0.0001, "Average rating should be 4.0 for a single review")
    }
}
