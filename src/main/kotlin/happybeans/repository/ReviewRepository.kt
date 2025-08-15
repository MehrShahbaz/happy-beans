package happybeans.repository

import happybeans.model.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository: JpaRepository<Review, Long> {
    fun findByUserId(userId: Long): List<Review>
    fun findByDishOptionId(dishId: Long): List<Review>
}