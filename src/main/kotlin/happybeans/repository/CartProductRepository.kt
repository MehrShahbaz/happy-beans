package happybeans.repository

import happybeans.model.CartProduct
import org.springframework.data.jpa.repository.JpaRepository

interface CartProductRepository : JpaRepository<CartProduct, Long> {
    fun findAllByUserId(userId: Long): List<CartProduct>

    fun deleteAllByUserId(userId: Long)

    fun deleteByUserIdAndDishOptionId(
        userId: Long,
        dishOptionId: Long,
    )

    fun findByUserIdAndDishOptionId(
        userId: Long,
        dishOptionId: Long,
    ): CartProduct?
}
