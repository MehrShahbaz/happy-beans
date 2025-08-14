package happybeans.controller.review

import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewCreateResponse
import happybeans.service.ReviewService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reviews")
class ReviewController(private val reviewService: ReviewService) {

    @PostMapping("/dish")
    fun createDishReview(@Valid @RequestBody dto: ReviewCreateRequestDto): ReviewCreateResponse {
        return reviewService.createDishReview(dto)
    }

    @PostMapping("/restaurant")
    fun createRestaurantReview(@Valid @RequestBody dto: ReviewCreateRequestDto): ReviewCreateResponse {
        return reviewService.createRestaurantReview(dto)
    }
}