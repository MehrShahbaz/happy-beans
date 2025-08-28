package happybeans.repository

import happybeans.model.DishOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DishOptionRepository : JpaRepository<DishOption, Long> {
    @Query(
        nativeQuery = true,
        value = """
        WITH filtered_options AS (
            SELECT dopt.*, 
                   COALESCE((
                       SELECT COUNT(*)
                       FROM dish_option_tags dot3
                       JOIN tags t3 ON dot3.tag_id = t3.id
                       WHERE dot3.dish_option_id = dopt.id 
                       AND t3.name IN :likedTagNames
                   ), 0) as like_count
            FROM dish_options dopt
            WHERE dopt.id NOT IN (
                SELECT DISTINCT do2.id 
                FROM dish_options do2 
                JOIN dish_option_tags dot2 ON do2.id = dot2.dish_option_id
                JOIN tags t2 ON dot2.tag_id = t2.id
                WHERE t2.name IN :dislikedTagNames
            )
        )
        SELECT id, available, created_at, description, dish_id, image, name, prep_time_minute, price, updated_at
        FROM filtered_options
        ORDER BY like_count DESC
        """,
    )
    fun findFilteredByUserPreferences(
        dislikedTagNames: Set<String>,
        likedTagNames: Set<String>,
    ): List<DishOption>
}
