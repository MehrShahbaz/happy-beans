package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    // For validating that the dish name is unique within the same restaurant
    @Query(
        "SELECT d FROM Dish d WHERE d.name = :name AND EXISTS " +
            "(SELECT r FROM Restaurant r WHERE r.id = :restaurantId AND d MEMBER OF r.dishes)",
    )
    fun findByNameAndRestaurantId(
        @Param("name") name: String,
        @Param("restaurantId") restaurantId: Long,
    ): Dish?

    @Query(
        "SELECT d FROM Dish d WHERE EXISTS (SELECT r FROM Restaurant r WHERE r.id = :restaurantId AND d MEMBER OF r.dishes)",
    )
    fun findDishesByRestaurant(
        @Param("restaurantId") restaurantId: Long,
        pageable: Pageable,
    ): Page<Dish>
}
