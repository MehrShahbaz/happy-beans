package happybeans.service

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishUpdateRequest
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
            restaurantRepository.findById(restaurantId)
                .orElseThrow { EntityNotFoundException("Restaurant not found") }

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

    @Transactional
    fun updateDish(
        dishId: Long,
        updateRequest: DishUpdateRequest,
    ): Dish {
        val dish = findById(dishId)

        // Check if new name conflicts with the existing dish
        val existingDishWithSameName = dishRepository.findByName(updateRequest.name)
        if (existingDishWithSameName != null && existingDishWithSameName.id != dishId) {
            throw DishAlreadyExistsException("Dish with name '${updateRequest.name}' already exists")
        }

        dish.name = updateRequest.name
        dish.description = updateRequest.description
        dish.image = updateRequest.image

        return dishRepository.save(dish)
    }

    @Transactional
    fun patchDish(
        dishId: Long,
        patchRequest: DishPatchRequest,
    ): Dish {
        val dish = findById(dishId)

        // Check if new name conflicts with existing dish (only if name is being updated)
        patchRequest.name?.let { newName ->
            val existingDishWithSameName = dishRepository.findByName(newName)
            if (existingDishWithSameName != null && existingDishWithSameName.id != dishId) {
                throw DishAlreadyExistsException("Dish with name '$newName' already exists")
            }
            dish.name = newName
        }

        // Update only provided fields
        patchRequest.description?.let { dish.description = it }
        patchRequest.image?.let { dish.image = it }

        return dishRepository.save(dish)
    }

    @Transactional
    fun addDishOption(
        dishId: Long,
        optionRequest: DishOptionCreateRequest,
    ): DishOption {
        val dish = findById(dishId)

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

        dish.addDishOption(dishOption)
        dishRepository.save(dish)
        return dishOption
    }

    @Transactional
    fun updateDishOption(
        dishId: Long,
        optionId: Long,
        updateRequest: DishOptionUpdateRequest,
    ): DishOption {
        val dishOption = findByIdAndDishOptionId(dishId, optionId)

        dishOption.name = updateRequest.name
        dishOption.description = updateRequest.description
        dishOption.price = updateRequest.price
        dishOption.image = updateRequest.image
        dishOption.prepTimeMinutes = updateRequest.prepTimeMinutes
        dishOption.rating = updateRequest.rating

        dishRepository.save(dishOption.dish)
        return dishOption
    }

    @Transactional
    fun patchDishOption(
        dishId: Long,
        optionId: Long,
        patchRequest: DishOptionPatchRequest,
    ): DishOption {
        val dishOption = findByIdAndDishOptionId(dishId, optionId)

        // Update only provided fields
        patchRequest.name?.let { dishOption.name = it }
        patchRequest.description?.let { dishOption.description = it }
        patchRequest.price?.let { dishOption.price = it }
        patchRequest.image?.let { dishOption.image = it }
        patchRequest.prepTimeMinutes?.let { dishOption.prepTimeMinutes = it }
        patchRequest.rating?.let { dishOption.rating = it }

        dishRepository.save(dishOption.dish)
        return dishOption
    }

    @Transactional
    fun deleteDishOption(
        dishId: Long,
        optionId: Long,
    ) {
        val dish = findById(dishId)
        val dishOption = findByIdAndDishOptionId(dishId, optionId)

        dish.removeDishOption(dishOption)
        dishRepository.save(dish)
    }

    @Transactional
    fun deleteDishById(dishId: Long) {
        val dish = findById(dishId)
        dishRepository.delete(dish)
    }
}
