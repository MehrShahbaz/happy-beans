package happybeans.repository

import happybeans.dto.dish.RecommendedDishDto
import happybeans.model.DishReview
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface DishReviewRepository : JpaRepository<DishReview, Long> {
    fun findByUserId(userId: Long): List<DishReview>

    fun findByDishOptionId(dishOptionId: Long): List<DishReview>

    @Query(
        """
    SELECT dr FROM DishReview dr
    JOIN DishOption do ON dr.dishOptionId = do.id
    WHERE do.dish.id = :dishId
    """,
    )
    fun findByDishId(dishId: Long): List<DishReview>

    @Query(
        """
        SELECT new happybeans.dto.dish.RecommendedDishDto(
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

    @Query(
        value = """
        SELECT AVG(rating)
        FROM dish_review
        WHERE dish_option_id = :dishOptionId
        """,
        nativeQuery = true,
    )
    fun findAverageDishOptionRating(
        @Param("dishOptionId") dishOptionId: Long,
    ): Double?
}
