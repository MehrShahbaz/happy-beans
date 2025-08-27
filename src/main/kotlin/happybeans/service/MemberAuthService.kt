package happybeans.service

import happybeans.dto.auth.AuthTokenPayload
import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.user.UserCreateRequestDto
import happybeans.dto.user.UserCreateResponse
import happybeans.infrastructure.JwtProvider
import happybeans.repository.UserRepository
import happybeans.utils.exception.UserAlreadyExistsException
import happybeans.utils.mapper.toEntity
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.net.URI

@Service
class MemberAuthService(
    val userRepository: UserRepository,
    val jwtProvider: JwtProvider,
    val loginService: LoginService,
) {
    private val logger = KotlinLogging.logger {}

    fun signUp(userCreateRequestDto: UserCreateRequestDto): UserCreateResponse {
        if (userRepository.existsByEmail(userCreateRequestDto.email)) {
            logger.error { "User already exists with email ${userCreateRequestDto.email}" }
            throw UserAlreadyExistsException(userCreateRequestDto.email)
        }
        val member = userRepository.save(userCreateRequestDto.toEntity())
        val authTokenPayload = jwtProvider.createToken(AuthTokenPayload(member.email))
        return UserCreateResponse(URI.create("/api/member/$member.id"), authTokenPayload)
    }

    fun login(loginRequestDto: LoginRequestDto): String {
        return loginService.login(loginRequestDto)
    }
}
