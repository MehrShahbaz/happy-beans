package happybeans.controller.dish

import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.toResponse
import happybeans.model.User
import happybeans.service.DishService
import happybeans.utils.annotations.LoginMember
import happybeans.utils.annotations.RestaurantOwner
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dishes/search")
class DishSearchController(
    private val dishService: DishService,
    ) {
        @GetMapping("/users}")
        fun getFilteredDishesByUser(
            @LoginMember user: User
        ): ResponseEntity<List<DishResponse>> {
            logger.info("GET Dishes by user tags for user: ${user.id}")
            val filteredDishes = dishService.getFilteredDishesByUser(user)
            return ResponseEntity.ok(filteredDishes.map { it.toResponse() })
        }
}