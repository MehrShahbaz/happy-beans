package happybeans.service

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.enums.TagContainerType
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.TagContainer
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.TagContainerRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
    private val tagContainerRepository: TagContainerRepository,
) {
    // TODO While creating first create a TagContainer with type INGRIDIENT and add it to the dish option

    fun findByRestaurantId(
        restaurantId: Long,
        pageable: Pageable,
    ): List<Dish> {
        return dishRepository.findByRestaurantId(restaurantId, pageable)
            ?: throw EntityNotFoundException("Restaurant with id $restaurantId not found")
    }

    fun findAll(pageable: Pageable): List<Dish> {
        return dishRepository.findAll(pageable).content
            ?: throw EntityNotFoundException("Dish not found")
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

        return dish.dishOption.firstOrNull { it.id == dishOptionId }
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

        val dish =
            Dish(
                name = dishRequest.name,
                description = dishRequest.description,
                image = dishRequest.image,
                dishOption = mutableSetOf(),
                prepTimeMinutes = 0,
                averageRating = 0.0,
            )
        val createdDishOptions = createAndLinkDishOptions(dish, dishRequest.dishOptionRequests)

        if (createdDishOptions.isNotEmpty()) {
            dish.averageRating = createdDishOptions.map { it.rating }.average()
        }
        restaurant.addDish(dish)
        val savedDish = dishRepository.save(dish)
        return savedDish
    }

    private fun createAndLinkDishOptions(
        dish: Dish,
        optionRequests: Set<DishOptionCreateRequest>,
    ): Set<DishOption> {
        return optionRequests.map { optionRequest ->
            val ingredientsContainer =
                TagContainer(
                    type = TagContainerType.INGREDIENTS,
                    dish = dish,
                )

            val dishOption =
                DishOption(
                    dish = dish,
                    name = optionRequest.name,
                    description = optionRequest.description,
                    price = optionRequest.price,
                    image = optionRequest.image,
                    ingredients = ingredientsContainer,
                    rating = optionRequest.rating,
                )
            dish.dishOption.add(dishOption)

            dishOption
        }.toSet()
    }

    @Transactional
    fun updateDishAverageRating(dishId: Long) {
        val dish =
            dishRepository.findByIdOrNull(dishId)
                ?: throw EntityNotFoundException("Dish with id $dishId not found")
        val averageRating = dish.dishOption.map { it.rating }.average()
        dish.averageRating = averageRating
    }
}
