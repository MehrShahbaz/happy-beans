package happybeans.config.interceptor

import happybeans.enums.UserRole
import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class RestaurantOwnerInterceptor(
    userRepository: UserRepository,
    jwtProvider: JwtProvider,
) : BaseAuthInterceptor(jwtProvider, userRepository) {
    private val logger = KotlinLogging.logger {}

    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        user: User,
    ): Boolean {
        if (user.role != UserRole.RESTAURANT_OWNER) {
            logger.error { "User ${user.id} is not restaurant owner" }
            throw UnauthorisedUserException("Only restaurant owners allowed")
        }

        return true
    }
}
