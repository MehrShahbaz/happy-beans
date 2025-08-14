package happybeans.service

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewCreateResponse
import happybeans.model.Review
import happybeans.repository.ReviewRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service

@Service
class ReviewService(
    private val reviewRepository: ReviewRepository
) {
    fun createDishReview(dto: ReviewCreateRequestDto): ReviewCreateResponse {
        require(dto.dishId != null) { "Dish ID is required for dish review" }
        require(dto.restaurantId == null) { "Restaurant ID must not be provided for dish review" }
//        if (dishRepository.existsById(dto.dishId)) {
//            throw EntityNotFoundException("Dish with ID ${dto.dishId} not found")
//        }

        val review = Review(
            userId = dto.userId,
            rating = dto.rating,
            message = dto.message,
            dishId = dto.dishId,
            restaurantId = null,
        )

        return reviewRepository.save(review).toReviewCreateResponse()
    }

    fun createRestaurantReview(dto: ReviewCreateRequestDto): ReviewCreateResponse {
        require(dto.restaurantId != null) { "Restaurant ID is required for restaurant review" }
        require(dto.dishId == null) { "Dish ID must not be provided for restaurant review" }
//        if (RestaurantRepository.existsById(dto.restaurantId)) {
//            throw IllegalArgumentException("Restaurant with ID ${dto.restaurantId} not found")
//        }
        val review = Review(
            userId = dto.userId,
            rating = dto.rating,
            message = dto.message,
            dishId = null,
            restaurantId = dto.restaurantId,
        )

        return reviewRepository.save(review).toReviewCreateResponse()

    }
    private fun Review.toReviewCreateResponse(): ReviewCreateResponse {
        return ReviewCreateResponse(
            id = this.id,
            userId = this.userId,
            rating = this.rating,
            message = this.message,
            dishId = this.dishId,
            restaurantId = this.restaurantId,
            createdAt = this.createdAt!!
        )
    }
}