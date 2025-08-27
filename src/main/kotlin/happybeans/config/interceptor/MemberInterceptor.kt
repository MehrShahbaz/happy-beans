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
class MemberInterceptor(
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
        if (user.role != UserRole.USER) {
            logger.error { "User ${user.id} is not user" }
            throw UnauthorisedUserException("Only User allowed")
        }

        return true
    }
}
