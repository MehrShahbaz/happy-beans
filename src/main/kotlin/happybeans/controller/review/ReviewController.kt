package happybeans.controller.review

import happybeans.dto.response.MessageResponse
import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.RestaurantReviewDto
import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.User
import happybeans.service.DishReviewService
import happybeans.service.RestaurantReviewService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/reviews")
class ReviewController(
    private val dishReviewService: DishReviewService,
    private val restaurantReviewService: RestaurantReviewService,
) {
    @PostMapping("/dish")
    fun createDishReview(
        @Valid @RequestBody dto: ReviewCreateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        val dishReviewId = dishReviewService.createDishReview(member, dto)
        return ResponseEntity.created(URI.create("/dish/$dishReviewId")).body(MessageResponse("Dish review created"))
    }

    @PatchMapping("/dish/{id}")
    fun updateDishReview(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ReviewUpdateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        dishReviewService.updateDishReview(id, dto, member)
        return ResponseEntity.ok(MessageResponse("Updated dish Review"))
    }

    @GetMapping("/dish")
    fun getAllDishReviews(): List<DishReviewDto> {
        return dishReviewService.getAllReviews()
    }

    @GetMapping("/dish/user")
    fun getDishReviewsByUserId(
        @PathVariable userId: Long,
        @LoginMember member: User,
    ): List<DishReviewDto> {
        return dishReviewService.getReviewsByUserId(member.id)
    }

    @GetMapping("/dish/dish-option/{dishOptionId}")
    fun getDishReviewsByDishOptionId(
        @PathVariable dishOptionId: Long,
    ): List<DishReviewDto> {
        return dishReviewService.getReviewsByDishOptionId(dishOptionId)
    }

    @GetMapping("/dish/average-rating/{dishOptionId}")
    fun getAverageRatingForDishOption(
        @PathVariable dishOptionId: Long,
    ): Double {
        return dishReviewService.getAverageRatingForDishOption(dishOptionId)
    }

    @PostMapping("/restaurant")
    fun createRestaurantReview(
        @Valid @RequestBody dto: ReviewCreateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        val restaurantReviewId = restaurantReviewService.createRestaurantReview(member, dto)
        return ResponseEntity.created(
            URI.create("/dish/$restaurantReviewId"),
        ).body(MessageResponse("Restaurant review created successfully"))
    }

    @PatchMapping("/restaurant/{id}")
    fun updateRestaurantReview(
        @PathVariable id: Long,
        @Valid @RequestBody dto: ReviewUpdateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        restaurantReviewService.updateRestaurantReview(id, dto, member)
        return ResponseEntity.ok(MessageResponse("Updated Restaurant Review"))
    }

    @GetMapping("/restaurant")
    fun getAllRestaurantReviews(): List<RestaurantReviewDto> {
        return restaurantReviewService.getAllReviews()
    }

    @GetMapping("/restaurant/user")
    fun getRestaurantReviewsByUserId(
        @PathVariable userId: Long,
        @LoginMember member: User,
    ): List<RestaurantReviewDto> {
        return restaurantReviewService.getReviewsByUserId(member.id)
    }

    @GetMapping("/restaurant/restaurant/{restaurantId}")
    fun getRestaurantReviewsByRestaurantId(
        @PathVariable restaurantId: Long,
    ): List<RestaurantReviewDto> {
        return restaurantReviewService.getReviewsByRestaurantId(restaurantId)
    }

    @GetMapping("/restaurant/average-rating/{restaurantId}")
    fun getAverageRatingForRestaurant(
        @PathVariable restaurantId: Long,
    ): Double {
        return restaurantReviewService.getAverageRatingForRestaurant(restaurantId)
    }
}
