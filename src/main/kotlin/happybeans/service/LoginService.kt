package happybeans.service

import happybeans.dto.auth.AuthTokenPayload
import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.infrastructure.JwtProvider
import happybeans.repository.UserRepository
import happybeans.utils.exception.UnauthorisedUserException
import happybeans.utils.exception.UserCredentialException
import org.springframework.stereotype.Service

@Service
class LoginService(
    private val userRepository: UserRepository,
    private val jwtProvider: JwtProvider,
) {
    fun login(
        loginRequestDto: LoginRequestDto,
        expectedRole: UserRole = UserRole.USER,
    ): String {
        val user =
            userRepository.findByEmailAndPassword(
                loginRequestDto.email,
                loginRequestDto.password,
            ).orElseThrow { UserCredentialException() }

        if (user.role != expectedRole) {
            throw UnauthorisedUserException("Incorrect role for this endpoint")
        }

        val token =
            jwtProvider.createToken(
                AuthTokenPayload(user.email),
            )
        return token
    }
}
