package happybeans.service

import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CreateRestaurantOwnerService(
    private val userRepository: UserRepository,
) {
    fun createRestaurantOwner(request: RestaurantOwnerRequestDto): User {
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
