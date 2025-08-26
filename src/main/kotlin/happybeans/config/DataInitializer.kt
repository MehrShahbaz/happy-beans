package happybeans.config

import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.enums.UserRole
import happybeans.model.Dish
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
    }
}
