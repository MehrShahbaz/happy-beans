package happybeans.repository

import happybeans.dto.restaurant.RecommendedRestaurantDto
import happybeans.model.RestaurantReview
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RestaurantReviewRepository : JpaRepository<RestaurantReview, Long> {
    fun findByUserId(userId: Long): List<RestaurantReview>

    fun findByRestaurantId(restaurantId: Long): List<RestaurantReview>

    @Query(
        """
        SELECT new happybeans.dto.restaurant.RecommendedRestaurantDto(
            r.restaurantId,
            r.restaurantName,
            AVG(r.rating)
        )
        FROM RestaurantReview r
        GROUP BY r.restaurantId, r.restaurantName
        ORDER BY AVG(r.rating) DESC
    """,
    )
    fun findHighestRatedRestaurants(pageable: Pageable): Page<RecommendedRestaurantDto>
}
