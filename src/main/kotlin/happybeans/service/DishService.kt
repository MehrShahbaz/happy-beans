package happybeans.service

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.DishAlreadyExistsException
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
) {
    fun findByName(name: String): Dish? {
        return dishRepository.findByName(name)
    }

    fun findAll(pageable: Pageable): List<Dish> {
        return dishRepository.findAll(pageable).content
    }

    fun findById(dishId: Long): Dish {
        return dishRepository.findByIdOrNull(dishId)
            ?: throw EntityNotFoundException("Dish with id $dishId not found")
    }

    fun findByIdAndDishOptionId(
        dishId: Long,
        dishOptionId: Long,
    ): DishOption {
        val dish = findById(dishId)

        return dish.dishOptions.firstOrNull { it.id == dishOptionId }
            ?: throw EntityNotFoundException("Dish with id $dishId and dish option id $dishOptionId not found")
    }

    @Transactional
    fun createDish(
        restaurantId: Long,
        dishRequest: DishCreateRequest,
    ): Dish {
        val restaurant =
            restaurantRepository.findByIdOrNull(restaurantId)
                ?: throw EntityNotFoundException("Restaurant with id $restaurantId not found")

        val existingDish = dishRepository.findByName(dishRequest.name)
        if (existingDish != null) {
            throw DishAlreadyExistsException("Dish with name '${dishRequest.name}' already exists")
        }

        val dish =
            Dish(
                name = dishRequest.name,
                description = dishRequest.description,
                image = dishRequest.image,
                dishOptions = mutableSetOf(),
            )
        val createdDishOptions = createAndLinkDishOptions(dish, dishRequest.dishOptionRequests)
        dish.dishOptions = createdDishOptions

        restaurant.addDish(dish)
        val savedDish = dishRepository.save(dish)
        return savedDish
    }

    private fun createAndLinkDishOptions(
        dish: Dish,
        optionRequests: Set<DishOptionCreateRequest>,
    ): MutableSet<DishOption> {
        return optionRequests.map { optionRequest ->
            val dishOption =
                DishOption(
                    dish = dish,
                    name = optionRequest.name,
                    description = optionRequest.description,
                    price = optionRequest.price,
                    image = optionRequest.image,
                    prepTimeMinutes = optionRequest.prepTimeMinutes,
                    rating = optionRequest.rating,
                )

            dishOption
        }.toMutableSet()
    }
}
