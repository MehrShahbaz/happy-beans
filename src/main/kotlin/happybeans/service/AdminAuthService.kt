package happybeans.service

import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class AdminAuthService(
    private val loginService: LoginService,
) {
    private val logger = KotlinLogging.logger {}

    fun login(loginRequest: LoginRequestDto): String {
        logger.info("Admin Login request: ${loginRequest.email}")
        return loginService.login(loginRequest, UserRole.ADMIN)
    }
}
