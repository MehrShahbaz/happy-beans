package happybeans.config.interceptor

import happybeans.enums.UserRole
import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class RestaurantOwnerInterceptor(
    userRepository: UserRepository,
    jwtProvider: JwtProvider,
) : BaseAuthInterceptor(jwtProvider, userRepository) {
    override fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        user: User,
    ): Boolean {
        if (user.role != UserRole.RESTAURANT_OWNER) {
            throw UnauthorisedUserException("Only restaurant owners allowed")
        }

        return true
    }
}
