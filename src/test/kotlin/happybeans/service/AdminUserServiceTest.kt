package happybeans.service

import happybeans.dto.user.UserCreateRequestDto
import happybeans.enums.UserRole
import happybeans.repository.UserRepository
import happybeans.utils.exception.DuplicateEntityException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminUserServiceTest {
    @Autowired
    lateinit var adminUserService: AdminUserService

    @Autowired
    lateinit var userRepository: UserRepository

    @AfterEach
    fun clean() {
        userRepository.deleteAll()
    }

    @Test
    fun createAdmin() {
        val admin =
            adminUserService.createAdmin(
                UserCreateRequestDto(
                    "admin-test@admin.com",
                    "12345678",
                    "test",
                    "test",
                ),
            )

        assertThat(admin.id).isNotZero
        assertThat(admin.role).isEqualTo(UserRole.ADMIN)
    }

    @Test
    fun `throws on duplicate email createAdmin`() {
        adminUserService.createAdmin(
            UserCreateRequestDto(
                "admin-test@admin.com",
                "12345678",
                "test",
                "test",
            ),
        )
        assertThrows<DuplicateEntityException> {
            adminUserService.createAdmin(
                UserCreateRequestDto(
                    "admin-test@admin.com",
                    "12345678",
                    "test",
                    "test",
                ),
            )
        }
    }
}
