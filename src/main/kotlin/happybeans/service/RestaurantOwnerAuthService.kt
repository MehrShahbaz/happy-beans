package happybeans.service

import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import org.springframework.stereotype.Service

@Service
class RestaurantOwnerAuthService(
    val loginService: LoginService,
) {
    fun login(loginRequestDto: LoginRequestDto): String {
        return loginService.login(loginRequestDto, UserRole.RESTAURANT_OWNER)
    }
}
