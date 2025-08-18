package happybeans.repository

import happybeans.dto.dish.RecommendedDishDto
import happybeans.model.DishReview
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DishReviewRepository : JpaRepository<DishReview, Long> {
    fun findByUserId(userId: Long): List<DishReview>

    fun findByDishOptionId(dishId: Long): List<DishReview>

    @Query(
        """
        SELECT new happybeans.dto.review.RecommendedDishDto(
            d.dishOptionId,
            d.dishOptionName,
            d.dishOptionPrice,
            AVG(d.rating)
        )
        FROM DishReview d
        GROUP BY d.dishOptionId, d.dishOptionName, d.dishOptionPrice
        ORDER BY AVG(d.rating) DESC
    """,
    )
    fun findHighestRatedDishes(): List<RecommendedDishDto>
}
