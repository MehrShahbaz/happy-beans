package happybeans.service

import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import org.springframework.stereotype.Service

@Service
class AdminAuthService(
    private val loginService: LoginService,
) {
    fun login(loginRequest: LoginRequestDto): String {
        return loginService.login(loginRequest, UserRole.ADMIN)
    }
}
