package happybeans.controller.review

import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.RestaurantReviewDto
import happybeans.service.DishReviewService
import happybeans.service.RestaurantReviewService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val dishReviewService: DishReviewService,
    private val restaurantReviewService: RestaurantReviewService,
) {
    @GetMapping("/dish")
    fun getAllDishReviews(): List<DishReviewDto> {
        return dishReviewService.getAllReviews()
    }

    @GetMapping("/dish/average-rating/{dishOptionId}")
    fun getAverageRatingForDishOption(
        @PathVariable dishOptionId: Long,
    ): Double {
        return dishReviewService.getAverageRatingForDishOption(dishOptionId)
    }

    @GetMapping("/restaurant")
    fun getAllRestaurantReviews(): List<RestaurantReviewDto> {
        return restaurantReviewService.getAllReviews()
    }

    @GetMapping("/restaurant/average-rating/{restaurantId}")
    fun getAverageRatingForRestaurant(
        @PathVariable restaurantId: Long,
    ): Double {
        return restaurantReviewService.getAverageRatingForRestaurant(restaurantId)
    }
}
