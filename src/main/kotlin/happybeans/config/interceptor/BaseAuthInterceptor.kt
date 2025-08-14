package happybeans.config.interceptor

import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerInterceptor

abstract class BaseAuthInterceptor(
    val jwtProvider: JwtProvider,
    val userRepository: UserRepository,
) : HandlerInterceptor {
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        val bearer = request.getHeader("Authorization") ?: throw UnauthorisedUserException()
        if (!bearer.startsWith("Bearer ")) {
            throw UnauthorisedUserException("Invalid Authorization header format")
        }

        val token = bearer.removePrefix("Bearer ").trim()
        jwtProvider.validateToken(token)

        val payload = jwtProvider.getPayload(token)

        val user =
            userRepository.findByEmail(payload.email).orElseThrow {
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
