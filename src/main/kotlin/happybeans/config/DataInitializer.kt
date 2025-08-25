package happybeans.config

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.enums.UserRole
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.UserRepository
import happybeans.service.DishService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer() : CommandLineRunner {
    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var dishRepository: DishRepository

    @Autowired
    private lateinit var dishService: DishService

    @Autowired
    lateinit var userRepository: UserRepository

    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            userRepository.save(
                User(
                    "admin@admin.com",
                    "12345678",
                    "admin",
                    "Admin",
                    UserRole.ADMIN,
                ),
            )
        }
        if (dishRepository.count() == 0L) {
            val owner =
                userRepository.save(
                    User(
                        "res-owner@owner.com",
                        "12345678",
                        "Restaurant",
                        "Owner",
                        UserRole.RESTAURANT_OWNER,
                    ),
                )
            val restaurant =
                restaurantRepository.save(
                    Restaurant(
                        owner,
                        "Restaurant 1",
                        "Restaurant Desc",
                        "",
                        "Restaurant",
                    ),
                )
            dishService.createDish(
                restaurant.id,
                DishCreateRequest(
                    "Dish 1",
                    "Dish Desc",
                    "",
                    mutableSetOf(
                        DishOptionCreateRequest(
                            "Dish Option 1",
                            "Dish Option Desc",
                            15.3,
                            "",
                            true,
                            25,
                        ),
                    ),
                ),
                owner,
            )
        }
    }
}
