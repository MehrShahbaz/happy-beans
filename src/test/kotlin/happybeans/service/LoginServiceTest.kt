package happybeans.service

import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import happybeans.utils.exception.UserCredentialException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class LoginServiceTest {
    @Autowired
    private lateinit var jwtProvider: JwtProvider

    @Autowired
    private lateinit var loginService: LoginService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Test
    fun `login with valid user`() {
        val user =
            userRepository.save(
                User(
                    "user-login@test.com",
                    "12345678",
                    "Test",
                    "User",
                ),
            )

        val loginRequestDto =
            LoginRequestDto(
                user.email,
                user.password,
            )

        val token = loginService.login(loginRequestDto)
        assertThat(token).startsWith("Bearer ")
        assertDoesNotThrow { jwtProvider.validateToken(token.removePrefix("Bearer ").trim()) }
    }

    @Test
    fun `throws if user does not exist`() {
        assertThrows<UserCredentialException> {
            loginService.login(
                LoginRequestDto(
                    "temp@temp.com",
                    "12345678",
                ),
            )
        }
    }

    @Test
    fun `throws if wrong password`() {
        userRepository.save(
            User(
                "user-login-1@test.com",
                "12345678",
                "Test",
                "User",
            ),
        )
        assertThrows<UserCredentialException> {
            loginService.login(
                LoginRequestDto(
                    "temp@temp.com",
                    "1234567999",
                ),
            )
        }
    }

    @Test
    fun `throws if wrong role`() {
        userRepository.save(
            User(
                "user-login-2@test.com",
                "12345678",
                "Test",
                "User",
                role = UserRole.ADMIN,
            ),
        )
        assertThrows<UnauthorisedUserException> {
            loginService.login(
                LoginRequestDto(
                    "user-login-2@test.com",
                    "12345678",
                ),
            )
        }
    }
}
