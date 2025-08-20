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
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map
import org.springframework.data.repository.findByIdOrNull

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
    private val tagContainerRepository: TagContainerRepository,
) {
    // TODO While creating first create a TagContainer with type INGREIDIENTS and add it to the dish option
    fun findByName(name: String): Dish? {
        return dishRepository.findByName(name)
    }

    fun findAll(pageable: Pageable): List<Dish> {
        return dishRepository.findAll(pageable).content
    }

    fun findDishById(dishId: Long): Dish? {
        return dishRepository.findByIdOrNull(dishId)
    }

    fun findByIdAndDishOptionId(
        dishId: Long,
        dishOptionId: Long,
    ): DishOption {
        val dish = dishRepository.findById(dishId)

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

        val dish =
            Dish(
                name = dishRequest.name,
                description = dishRequest.description,
                image = dishRequest.image,
                dishOptions = mutableSetOf(),
            )
        val createdDishOptions = createAndLinkDishOptions(dish, dishRequest.dishOptionRequests)

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
                    //TODO: replace? (dish = dish with user = null)
                    dish = dish,
                )

            val dishOption =
                DishOption(q
                    dish = dish,
                    name = optionRequest.name,
                    description = optionRequest.description,
                    price = optionRequest.price,
                    image = optionRequest.image,
                    ingredients = ingredientsContainer,
                    prepTimeMinutes = optionRequest.prepTimeMinutes,
                    rating = optionRequest.rating,
                )
            dish.dishOptions.add(dishOption)

            dishOption
        }.toSet()
    }

}
