package happybeans.config.argumentResolver

import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.annotations.LoginMember
import happybeans.utils.exception.UnauthorisedUserException
import mu.KotlinLogging
import org.springframework.core.MethodParameter
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class LoginMemberArgumentResolver(
    private val userRepository: UserRepository,
) : HandlerMethodArgumentResolver {
    private val logger = KotlinLogging.logger {}

    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.hasParameterAnnotation(LoginMember::class.java)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): User {
        val request = (webRequest as ServletWebRequest).request

        logger.debug("Loading email from request")
        val email = request.getAttribute("email") as String

        logger.debug("Finding user with email: $email")
        val user =
            userRepository.findByEmail(email).orElseThrow {
                logger.error("Error finding user with email $email")
                UnauthorisedUserException("User not found")
            }
        if (user.role != UserRole.USER) {
            logger.error("User role not valid User:${user.id}")
            throw UnauthorisedUserException("User role not valid")
        }

        return user
    }
}
