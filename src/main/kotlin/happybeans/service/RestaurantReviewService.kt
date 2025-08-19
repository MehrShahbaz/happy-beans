package happybeans.service

import happybeans.dto.restaurant.RecommendedRestaurantDto
import happybeans.dto.review.RestaurantReviewDto
import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.RestaurantReview
import happybeans.model.User
import happybeans.repository.RestaurantRepository
import happybeans.repository.RestaurantReviewRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class RestaurantReviewService(
    private val restaurantReviewRepository: RestaurantReviewRepository,
    private val restaurantRepository: RestaurantRepository,
) {
    fun createRestaurantReview(
        member: User,
        dto: ReviewCreateRequestDto,
    ): Long {
        val restaurant =
            restaurantRepository.findByIdOrNull(dto.entityId)
                ?: throw IllegalArgumentException("Restaurant with ID ${dto.entityId} not found")

        val review =
            RestaurantReview(
                userId = member.id,
                userName = member.firstName,
                rating = dto.rating,
                message = dto.message,
                restaurantId = restaurant.id,
                restaurantName = restaurant.name,
            )

        return restaurantReviewRepository.save(review).id
    }

    fun updateRestaurantReview(
        id: Long,
        dto: ReviewUpdateRequestDto,
        member: User,
    ) {
        val review =
            restaurantReviewRepository.findByIdOrNull(id)
                ?: throw EntityNotFoundException("Review with ID $id not found")

        review.message = dto.message

        restaurantReviewRepository.save(review)
    }

    fun getAllReviews(): List<RestaurantReviewDto> {
        return restaurantReviewRepository.findAll().map { it.toReviewDto() }
    }

    fun getReviewsByUserId(userId: Long): List<RestaurantReviewDto> {
        return restaurantReviewRepository.findByUserId(userId).map { it.toReviewDto() }
    }

    fun getReviewsByRestaurantId(restaurantId: Long): List<RestaurantReviewDto> {
        return restaurantReviewRepository.findByRestaurantId(restaurantId).map { it.toReviewDto() }
    }

    fun getAverageRatingForRestaurant(restaurantId: Long): Double {
        val reviews = restaurantReviewRepository.findByRestaurantId(restaurantId)
        if (reviews.isEmpty()) {
            return 0.0
        }
        val average = reviews.map { it.rating }.average()
        return average.toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()
    }

    fun recommendHighestRatedRestaurants(pageable: Pageable): Page<RecommendedRestaurantDto> {
        return restaurantReviewRepository.findHighestRatedRestaurants(pageable)
    }

    private fun RestaurantReview.toReviewDto(): RestaurantReviewDto {
        return RestaurantReviewDto(
            id = this.id,
            userName = this.userName,
            rating = this.rating,
            message = this.message,
            restaurantId = this.restaurantId!!,
            restaurantName = this.restaurantName,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
        )
    }
}
