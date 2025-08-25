package happybeans.controller.member

import happybeans.dto.response.MessageResponse
import happybeans.dto.review.RestaurantReviewDto
import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.User
import happybeans.service.RestaurantReviewService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/member/review/restaurant")
class MemberRestaurantReviewController(
    private val restaurantReviewService: RestaurantReviewService,
) {
    @PostMapping("")
    fun createRestaurantReview(
        @Valid @RequestBody dto: ReviewCreateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        val restaurantReviewId = restaurantReviewService.createRestaurantReview(member, dto)
        return ResponseEntity.created(
            URI.create("$restaurantReviewId"),
        ).body(MessageResponse("Restaurant review created successfully"))
    }

    @PatchMapping("/{restaurantReviewId}")
    fun updateRestaurantReview(
        @PathVariable restaurantReviewId: Long,
        @Valid @RequestBody dto: ReviewUpdateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        restaurantReviewService.updateRestaurantReview(restaurantReviewId, dto, member)
        return ResponseEntity.ok(MessageResponse("Updated Restaurant Review"))
    }

    @GetMapping("/{restaurantId}")
    fun getRestaurantReviewsByRestaurantId(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<List<RestaurantReviewDto>> {
        return ResponseEntity.ok(restaurantReviewService.getReviewsByRestaurantId(restaurantId))
    }

    @GetMapping("/user/{userId}")
    fun getRestaurantReviewsByUserId(
        @PathVariable userId: Long,
        @LoginMember member: User,
    ): ResponseEntity<List<RestaurantReviewDto>> {
        return ResponseEntity.ok(restaurantReviewService.getReviewsByUserId(member.id))
    }

    @DeleteMapping("/{restaurantId}")
    fun deleteRestaurantReviewById(
        @PathVariable restaurantId: Long,
    ): ResponseEntity<Unit> {
        restaurantReviewService.deleteReviewById(restaurantId)
        return ResponseEntity.noContent().build()
    }
}
