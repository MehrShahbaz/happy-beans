package happybeans.service

import happybeans.dto.auth.AuthTokenPayload
import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.user.UserCreateRequestDto
import happybeans.dto.user.UserCreateResponse
import happybeans.enums.TagContainerType
import happybeans.infrastructure.JwtProvider
import happybeans.model.TagContainer
import happybeans.model.User
import happybeans.repository.TagContainerRepository
import happybeans.repository.UserRepository
import happybeans.utils.exception.UserAlreadyExistsException
import happybeans.utils.mapper.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.net.URI

@Service
class MemberAuthService(
    val userRepository: UserRepository,
    val jwtProvider: JwtProvider,
    val loginService: LoginService,
    val tagContainerRepository: TagContainerRepository,
) {
    @Transactional
    fun signUp(userCreateRequestDto: UserCreateRequestDto): UserCreateResponse {
        if (userRepository.existsByEmail(userCreateRequestDto.email)) {
            throw UserAlreadyExistsException(userCreateRequestDto.email)
        }
        val member = userRepository.save(userCreateRequestDto.toEntity())
        userRepository.flush()
        val list = createLikesAndDislikes(member)
        tagContainerRepository.saveAll(list)
        val authTokenPayload = jwtProvider.createToken(AuthTokenPayload(member.email))
        return UserCreateResponse(URI.create("/api/member/$member.id"), "Bearer $authTokenPayload")
    }

    fun login(loginRequestDto: LoginRequestDto): String {
        return loginService.login(loginRequestDto)
    }

    private fun createLikesAndDislikes(member: User): List<TagContainer> {
        return listOf(
            TagContainer(
                type = TagContainerType.LIKES,
                user = member,
            ),
            TagContainer(
                type = TagContainerType.DISLIKES,
                user = member,
            ),
        )
    }
}
