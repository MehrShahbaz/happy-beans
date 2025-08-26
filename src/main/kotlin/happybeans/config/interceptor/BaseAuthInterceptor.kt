package happybeans.config.interceptor

import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import mu.KotlinLogging
import org.springframework.web.servlet.HandlerInterceptor

abstract class BaseAuthInterceptor(
    val jwtProvider: JwtProvider,
    val userRepository: UserRepository,
) : HandlerInterceptor {
    private val logger = KotlinLogging.logger {}

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val bearer = request.getHeader("Authorization") ?: throw UnauthorisedUserException()
        if (!bearer.startsWith("Bearer ")) {
            logger.error("Authorization header is missing")
            throw UnauthorisedUserException("Invalid Authorization header format")
        }

        val token = bearer.removePrefix("Bearer ").trim()
        jwtProvider.validateToken(token)

        val payload = jwtProvider.getPayload(token)

        val user =
            userRepository.findByEmail(payload.email).orElseThrow {
                logger.error("User not found with email ${payload.email}")
                throw UnauthorisedUserException("User not found")
            }

        request.setAttribute("email", payload.email)

        return handleAuthenticatedRequest(request, response, handler, user)
    }

    abstract fun handleAuthenticatedRequest(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        user: User,
    ): Boolean
}
