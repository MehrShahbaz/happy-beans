package happybeans.service

import happybeans.dto.user.UserCreateRequestDto
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.UserAlreadyExistsException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MemberAuthServiceTest {
    @Autowired
    private lateinit var memberAuthService: MemberAuthService

    @Autowired
    private lateinit var userRepository: UserRepository
    @Test
    fun `throws error if already exists signUp`() {
        val userDTO = UserCreateRequestDto(
            "temp@temp.com",
            "test-456",
            "First",
            "Last"
        )
        val member =
            User(
                userDTO.email,
                userDTO.password,
                userDTO.firstName,
                userDTO.lastName,
            )
        userRepository.save(member)
        assertThrows<UserAlreadyExistsException> { memberAuthService.signUp(userDTO) }
    }

    @Test
    fun signUp() {
        val userDTO = UserCreateRequestDto(
            "temp-2@temp.com",
            "test-456",
            "First",
            "Last"
        )
        val result = memberAuthService.signUp(userDTO)
        assertThat(result.token).isNotEmpty
        assertThat(result.uri).isNotNull
    }
}