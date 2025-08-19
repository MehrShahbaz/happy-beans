package happybeans.service

import happybeans.dto.dish.RecommendedDishDto
import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.DishReview
import happybeans.model.User
import happybeans.repository.DishOptionRepository
import happybeans.repository.DishReviewRepository
import jakarta.persistence.EntityNotFoundException
import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Transactional
@Service
class DishReviewService(
    private val dishReviewRepository: DishReviewRepository,
    private val dishOptionRepository: DishOptionRepository,
) {
    fun createDishReview(
        member: User,
        dto: ReviewCreateRequestDto,
    ): Long {
        val dishOption =
            dishOptionRepository.findByIdOrNull(dto.entityId)
                ?: throw EntityNotFoundException("Dish option with ID ${dto.entityId} not found")

        val review =
            DishReview(
                userId = member.id,
                userName = member.firstName,
                rating = dto.rating,
                message = dto.message,
                dishOptionId = dishOption.id,
                dishOptionName = dishOption.name,
                dishOptionPrice = dishOption.price,
            )

        return dishReviewRepository.save(review).id
    }

    fun updateDishReview(
        id: Long,
        dto: ReviewUpdateRequestDto,
        member: User,
    ) {
        val review =
            dishReviewRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Review with ID $id not found") }

        review.message = dto.message
        dishReviewRepository.save(review)
    }

    fun getAllReviews(): List<DishReviewDto> {
        return dishReviewRepository.findAll().map { it.toReviewDto() }
    }

    fun getReviewsByDishOptionId(dishOptionId: Long): List<DishReviewDto> {
        return dishReviewRepository.findByDishOptionId(dishOptionId).map { it.toReviewDto() }
    }

    fun getReviewsByUserId(userId: Long): List<DishReviewDto> {
        return dishReviewRepository.findByUserId(userId).map { it.toReviewDto() }
    }

    fun getReviewsByDishId(dishId: Long): List<DishReviewDto> {
        return dishReviewRepository.findByDishId(dishId).map { it.toReviewDto() }
    }

    fun getAverageRatingForDishOption(dishOptionId: Long): Double {
        val reviews = dishReviewRepository.findByDishOptionId(dishOptionId)
        if (reviews.isEmpty()) {
            return 0.0
        }
        val average = reviews.map { it.rating }.average()
        return average.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    fun recommendHighestRatedDishes(): List<RecommendedDishDto> {
        return dishReviewRepository.findHighestRatedDishes()
    }

    private fun DishReview.toReviewDto(): DishReviewDto {
        return DishReviewDto(
            id = this.id,
            userName = this.userName,
            rating = this.rating,
            message = this.message,
            dishOptionId = this.dishOptionId!!,
            dishOptionName = this.dishOptionName,
            dishOptionPrice = this.dishOptionPrice,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}
