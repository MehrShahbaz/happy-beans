package happybeans.controller.review

import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.RestaurantReviewDto
import happybeans.service.DishReviewService
import happybeans.service.RestaurantReviewService
import mu.KotlinLogging
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/owner/reviews")
class ReviewController(
    private val dishReviewService: DishReviewService,
    private val restaurantReviewService: RestaurantReviewService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/dish")
    fun getAllDishReviews(): List<DishReviewDto> {
        logger.info { "Getting all reviews" }
        return dishReviewService.getAllReviews()
    }

    @GetMapping("/dish/average-rating/{dishOptionId}")
    fun getAverageRatingForDishOption(
        @PathVariable dishOptionId: Long,
    ): Double {
        logger.info("Getting rating for $dishOptionId")
        return dishReviewService.getAverageRatingForDishOption(dishOptionId)
    }

    @GetMapping("/restaurant")
    fun getAllRestaurantReviews(): List<RestaurantReviewDto> {
        logger.info { "Getting all restaurant reviews" }
        return restaurantReviewService.getAllReviews()
    }

    @GetMapping("/restaurant/average-rating/{restaurantId}")
    fun getAverageRatingForRestaurant(
        @PathVariable restaurantId: Long,
    ): Double {
        logger.info { "Getting restaurant reviews for $restaurantId" }
        return restaurantReviewService.getAverageRatingForRestaurant(restaurantId)
    }
}
