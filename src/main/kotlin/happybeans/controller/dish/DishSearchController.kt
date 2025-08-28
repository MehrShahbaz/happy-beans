package happybeans.controller.dish

import happybeans.model.DishOption
import happybeans.model.User
import happybeans.service.DishService
import happybeans.service.UserService
import happybeans.utils.annotations.LoginMember
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member/dishes/search")
class DishSearchController(
    private val dishService: DishService,
    private val userService: UserService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping()
    fun getFilteredDishOptionsByUser(
        @LoginMember user: User,
    ): ResponseEntity<List<DishOption>> {
        logger.info("GET Dishes by user tags for user: ${user.id}")

        val likes = userService.getUserLikes(user.id).map { it.name }.toSet()
        val dislikes = userService.getUserDislikes(user.id).map { it.name }.toSet()

        val filteredDishOptions = dishService.getFilteredDishOptionsByUserTags(likes, dislikes)
        return ResponseEntity.ok(filteredDishOptions)
    }
}
