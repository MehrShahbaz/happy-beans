package happybeans.controller.member

import happybeans.dto.response.MessageResponse
import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.ReviewCreateRequestDto
import happybeans.dto.review.ReviewUpdateRequestDto
import happybeans.model.User
import happybeans.service.DishReviewService
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
@RequestMapping("/api/dishOption-review")
class MemberDishReviewController
(private val dishReviewService: DishReviewService) {
    @PostMapping("")
    fun createDishReview(
        @Valid @RequestBody dto: ReviewCreateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        val dishReviewId = dishReviewService.createDishReview(member, dto)
        return ResponseEntity.created(URI.create("$dishReviewId")).body(MessageResponse("Dish review created"))
    }

    @PatchMapping("/{dishReviewId}")
    fun updateDishReview(
        @PathVariable dishReviewId: Long,
        @Valid @RequestBody dto: ReviewUpdateRequestDto,
        @LoginMember member: User,
    ): ResponseEntity<MessageResponse> {
        dishReviewService.updateDishReview(dishReviewId, dto, member)
        return ResponseEntity.ok(MessageResponse("Updated dish Review"))
    }

    @GetMapping("/user")
    fun getDishReviewsByUserId(
        @PathVariable userId: Long,
        @LoginMember member: User,
    ): List<DishReviewDto> {
        return dishReviewService.getReviewsByUserId(member.id)
    }

    @GetMapping("/{dishOptionId}")
    fun getDishReviewsByDishOptionId(
        @PathVariable dishOptionId: Long,
    ): List<DishReviewDto> {
        return dishReviewService.getReviewsByDishOptionId(dishOptionId)
    }

    @GetMapping("/{dishId}")
    fun getDishReviewsByDishId(
        @PathVariable dishId: Long,
    ): List<DishReviewDto> {
        return dishReviewService.getReviewsByDishId(dishId)
    }
}
