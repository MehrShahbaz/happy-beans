package happybeans.service

import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.dto.dish.DishPatchRequest
import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.DishUpdateRequest
import happybeans.dto.dish.toResponse
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.Restaurant
import happybeans.model.Tag
import happybeans.model.User
import happybeans.repository.DishOptionRepository
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.utils.exception.DishAlreadyExistsException
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.collections.map

@Service
class DishService(
    private val dishRepository: DishRepository,
    private val restaurantRepository: RestaurantRepository,
    private val dishOptionRepository: DishOptionRepository,
    private val tagService: TagService,
) {
    @Transactional(readOnly = true)
    fun getAllDishes(): List<DishResponse> {
        return dishRepository.findAll().map { it.toResponse()}
    }

    @Transactional(readOnly = true)
    fun findDishesByRestaurant(
        restaurantId: Long,
        pageable: Pageable,
    ): Page<Dish> {
        return dishRepository.findDishesByRestaurant(restaurantId, pageable)
    }

    fun findByNameAndRestaurant(
        name: String,
        restaurantId: Long,
    ): Dish? {
        return dishRepository.findByNameAndRestaurantId(name, restaurantId)
    }

    fun findById(dishId: Long): Dish {
        return dishRepository.findById(dishId).orElseThrow { EntityNotFoundException("Dish with id $dishId not found") }
    }

    @Transactional(readOnly = true)
    fun findByIdAndDishOptionId(
        dishId: Long,
        dishOptionId: Long,
    ): DishOption {
        val dish = findById(dishId)

        return dish.dishOptions.firstOrNull { it.id == dishOptionId }
            ?: throw EntityNotFoundException("Dish with id $dishId and dish option id $dishOptionId not found")
    }

    fun findDishOptionById(dishOptionId: Long): DishOption {
        return dishOptionRepository.findById(dishOptionId)
            .orElseThrow { EntityNotFoundException("Dish option with id $dishOptionId not found") }
    }

    @Transactional
    fun createDish(
        restaurantId: Long,
        dishRequest: DishCreateRequest,
        owner: User,
    ): Dish {
        val restaurant = findValidRestaurant(restaurantId, owner.id)

        val existingDish = dishRepository.findByNameAndRestaurantId(dishRequest.name, restaurantId)
        if (existingDish != null) {
            throw DishAlreadyExistsException(
                "Dish with name '${dishRequest.name}' already exists in this restaurant with id '$restaurantId'",
            )
        }

        val dish =
            Dish(
                name = dishRequest.name,
                description = dishRequest.description,
                image = dishRequest.image,
                dishOptions = mutableSetOf(),
            )
        dish.dishOptions = createAndLinkDishOptions(dish, dishRequest.dishOptionRequests)

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
                    available = optionRequest.available,
                    description = optionRequest.description,
                    price = optionRequest.price,
                    image = optionRequest.image,
                    prepTimeMinutes = optionRequest.prepTimeMinutes,
                )

            dishOption
        }.toMutableSet()
    }

    @Transactional
    fun updateDish(
        dishId: Long,
        updateRequest: DishUpdateRequest,
        owner: User,
    ): Dish {
        val dish = findById(dishId)

        val restaurant = findValidRestaurantByDishId(dish.id, owner.id)

        // Check if new name conflicts with another dish in the same restaurant
        val existingDishWithSameName = dishRepository.findByNameAndRestaurantId(updateRequest.name, restaurant.id)
        if (existingDishWithSameName != null && existingDishWithSameName.id != dishId) {
            throw DishAlreadyExistsException("Dish with name '${updateRequest.name}' already exists in this restaurant")
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
        owner: User,
    ): Dish {
        val dish = findById(dishId)

        // Check if new name conflicts with the existing dish (only if name is being updated)
        patchRequest.name?.let { newName ->
            val restaurant = findValidRestaurantByDishId(dish.id, owner.id)

            val existingDishWithSameName = dishRepository.findByNameAndRestaurantId(newName, restaurant.id)
            if (existingDishWithSameName != null && existingDishWithSameName.id != dishId) {
                throw DishAlreadyExistsException("Dish with name '$newName' already exists in this restaurant")
            }
            dish.name = newName
        }

        patchRequest.description?.let { dish.description = it }
        patchRequest.image?.let { dish.image = it }

        return dishRepository.save(dish)
    }

    @Transactional
    fun addDishOption(
        dishId: Long,
        optionRequest: DishOptionCreateRequest,
        owner: User,
    ): DishOption {
        val dish = findById(dishId)

        val dishOption =
            DishOption(
                dish = dish,
                name = optionRequest.name,
                available = optionRequest.available,
                description = optionRequest.description,
                price = optionRequest.price,
                image = optionRequest.image,
                prepTimeMinutes = optionRequest.prepTimeMinutes,
            )

        findValidRestaurantByDishId(dishId, owner.id)
        dish.addDishOption(dishOption)
        dishRepository.save(dish)
        return dishOption
    }

    @Transactional
    fun updateDishOption(
        optionId: Long,
        updateRequest: DishOptionUpdateRequest,
        owner: User,
    ): DishOption {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id

        findValidRestaurantByDishId(dishId, owner.id)

        dishOption.name = updateRequest.name
        dishOption.description = updateRequest.description
        dishOption.price = updateRequest.price
        dishOption.image = updateRequest.image
        dishOption.prepTimeMinutes = updateRequest.prepTimeMinutes

        dishRepository.save(dishOption.dish)
        return dishOption
    }

    @Transactional
    fun patchDishOption(
        optionId: Long,
        patchRequest: DishOptionPatchRequest,
        owner: User,
    ): DishOption {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id

        findValidRestaurantByDishId(dishId, owner.id)

        // Update only provided fields
        patchRequest.name?.let { dishOption.name = it }
        patchRequest.description?.let { dishOption.description = it }
        patchRequest.price?.let { dishOption.price = it }
        patchRequest.image?.let { dishOption.image = it }
        patchRequest.prepTimeMinutes?.let { dishOption.prepTimeMinutes = it }

        dishRepository.save(dishOption.dish)
        return dishOption
    }

    @Transactional
    fun deleteDishOption(
        optionId: Long,
        owner: User,
    ) {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id
        val dish = findById(dishId)

        findValidRestaurantByDishId(dishId, owner.id)

        dish.removeDishOption(dishOption)
        dishRepository.save(dish)
    }

    @Transactional
    fun deleteDishById(
        dishId: Long,
        owner: User,
    ) {
        val dish = findById(dishId)
        findValidRestaurantByDishId(dishId, owner.id)
        dishRepository.delete(dish)
    }

    @Transactional
    fun enableDishOption(
        dishId: Long,
        optionId: Long,
        owner: User,
    ): DishOption {
        val dish = findById(dishId)
        findValidRestaurantByDishId(dishId, owner.id)
        dish.enableDishOption(optionId)
        dishRepository.save(dish)
        return findByIdAndDishOptionId(dishId, optionId)
    }

    @Transactional
    fun disableDishOption(
        dishId: Long,
        optionId: Long,
        owner: User,
    ): DishOption {
        val dish = findById(dishId)
        findValidRestaurantByDishId(dishId, owner.id)
        dish.disableDishOption(optionId)
        dishRepository.save(dish)
        return findByIdAndDishOptionId(dishId, optionId)
    }

    @Transactional
    fun setDishOptionAvailability(
        dishId: Long,
        optionId: Long,
        available: Boolean,
        owner: User,
    ): DishOption {
        val dish = findById(dishId)
        findValidRestaurantByDishId(dishId, owner.id)
        dish.setDishOptionAvailability(optionId, available)
        dishRepository.save(dish)
        return findByIdAndDishOptionId(dishId, optionId)
    }

    @Transactional(readOnly = true)
    fun getAvailableDishOptions(dishId: Long): List<DishOption> {
        val dish = findById(dishId)
        return dish.getAvailableOptions()
    }

    @Transactional(readOnly = true)
    fun isDishAvailable(dishId: Long): Boolean {
        val dish = findById(dishId)
        return dish.hasAvailableOptions()
    }

    @Transactional
    fun addDishOptionTag(
        optionId: Long,
        tagName: String,
        owner: User,
    ): DishOption {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id
        findValidRestaurantByDishId(dishId, owner.id)

        val tag = tagService.findOrCreateByName(tagName)
        dishOption.dishOptionTags.add(tag)

        val dish = findById(dishId)
        return dishRepository.save(dish).dishOptions.first { it.id == optionId }
    }

    @Transactional
    fun removeDishOptionTag(
        optionId: Long,
        tagName: String,
        owner: User,
    ): DishOption {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id
        findValidRestaurantByDishId(dishId, owner.id)

        val tag = tagService.findByName(tagName)
        if (tag != null) {
            dishOption.dishOptionTags.remove(tag)
        }

        val dish = findById(dishId)
        return dishRepository.save(dish).dishOptions.first { it.id == optionId }
    }

    @Transactional
    fun updateDishOptionTags(
        optionId: Long,
        tagNames: Set<String>,
        owner: User,
    ): DishOption {
        val dishOption = findDishOptionById(optionId)
        val dishId = dishOption.dish.id
        findValidRestaurantByDishId(dishId, owner.id)

        dishOption.dishOptionTags.clear()
        tagNames.forEach { tagName ->
            val tag = tagService.findOrCreateByName(tagName)
            dishOption.dishOptionTags.add(tag)
        }

        val dish = findById(dishId)
        return dishRepository.save(dish).dishOptions.first { it.id == optionId }
    }

    @Transactional(readOnly = true)
    fun getDishOptionTags(optionId: Long): Set<Tag> {
        val dishOption = findDishOptionById(optionId)
        return dishOption.dishOptionTags
    }

    private fun findValidRestaurant(
        restaurantId: Long,
        userId: Long,
    ): Restaurant {
        return restaurantRepository.findByIdAndUserId(restaurantId, userId)
            ?: throw EntityNotFoundException("Restaurant with id '$restaurantId' not found for user with id '$userId'")
    }

    private fun findValidRestaurantByDishId(
        dishId: Long,
        userId: Long,
    ): Restaurant {
        val dish = findById(dishId)

        val restaurant =
            restaurantRepository.findAll().firstOrNull { it.dishes.contains(dish) }
                ?: throw EntityNotFoundException("Restaurant for dish not found")

        return findValidRestaurant(restaurant.id, userId)
    }
}
