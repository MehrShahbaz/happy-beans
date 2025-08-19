package happybeans.repository

import happybeans.model.Dish
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DishRepository : JpaRepository<Dish, Long> {
    fun findByName(name: String): Dish?

//    fun findByNameOrNull(name: String): Dish?

    //TODO check ; needed?
    //fun findByIdOrNull(id: Long): Dish?


//    fun findAllOrNull(pageable: Pageable): List<Dish>?


//    @Query("SELECT d FROM Dish d WHERE d.id IN (SELECT r.dishes FROM Restaurant r WHERE r.id = :restaurantId)")
//    fun findDishesByRestaurantId(
//        restaurantId: Long,
//        pageable: Pageable,
//    ): List<Dish>
}
