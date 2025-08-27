package happybeans.controller.dish

import happybeans.model.DishOption
import happybeans.model.User
import happybeans.service.DishService
import happybeans.utils.annotations.LoginMember
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dishes/search")
class DishSearchController(
    private val dishService: DishService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/users")
    fun getFilteredDishOptionsByUser(
        @LoginMember user: User,
    ): ResponseEntity<List<DishOption>> {
        logger.info("GET Dishes by user tags for user: ${user.id}")
        val filteredDishOptions = dishService.getFilteredDishOptionsByUser(user)
        return ResponseEntity.ok(filteredDishOptions)
    }
}
