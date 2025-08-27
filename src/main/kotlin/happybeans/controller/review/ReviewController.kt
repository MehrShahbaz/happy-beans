package happybeans.controller.review

import happybeans.service.DishReviewService
import happybeans.service.RestaurantReviewService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/restaurant-owner/reviews")
class ReviewController(
    private val dishReviewService: DishReviewService,
    private val restaurantReviewService: RestaurantReviewService,
) {
    // TODO Make this protected for only the reviewer
//    @GetMapping("/dish")
//    fun getAllDishReviews(): List<DishReviewDto> {
//        return dishReviewService.getAllReviews()
//    }

    // TODO Dish not available
//    @GetMapping("/dish/average-rating/{dishOptionId}")
//    fun getAverageRatingForDishOption(
//        @PathVariable dishOptionId: Long,
//    ): Double {
//        return dishReviewService.getAverageRatingForDishOption(dishOptionId)
//    }

    // TODO Owners res
//    @GetMapping("/restaurant")
//    fun getAllRestaurantReviews(): List<RestaurantReviewDto> {
//        return restaurantReviewService.getAllReviews()
//    }

    // TODO Protection for Res Owner
//    @GetMapping("/restaurant/average-rating/{restaurantId}")
//    fun getAverageRatingForRestaurant(
//        @PathVariable restaurantId: Long,
//    ): Double {
//        return restaurantReviewService.getAverageRatingForRestaurant(restaurantId)
//    }
}
