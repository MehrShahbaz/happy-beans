package happybeans.service

import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.DuplicateEntityException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateRestaurantOwnerService(
    private val userRepository: UserRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun createRestaurantOwner(request: RestaurantOwnerRequestDto): User {
        if (userRepository.existsByEmailAndRole(request.email, UserRole.RESTAURANT_OWNER)) {
            logger.error { "Restaurant owner with email: ${request.email} already exists" }
            throw DuplicateEntityException("Restaurant owner with email: ${request.email} already exists")
        }
        return userRepository.save(
            User(
                request.email,
                createPassword(request.firstName),
                request.firstName,
                request.lastName,
                UserRole.RESTAURANT_OWNER,
            ),
        )
    }

    private fun createPassword(firstName: String): String {
        return RESTAURANT_OWNER_PASSWORD + "-" + firstName.first()
    }

    companion object {
        private const val RESTAURANT_OWNER_PASSWORD = "password"
    }
}
