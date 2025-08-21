package happybeans.service

import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class CreateRestaurantOwnerServiceTest {
    @Autowired
    lateinit var service: CreateRestaurantOwnerService

    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun createRestaurantOwner() {
        val request =
            RestaurantOwnerRequestDto(
                "owner-12@res.com",
                "Hello",
                "World",
            )
        val user = service.createRestaurantOwner(request)

        assertThat(user.id).isNotNull
        assertThat(user.password).isEqualTo("password-${request.firstName.first()}")
    }

    @Test
    fun `throws error if restaurant owner already exists createRestaurantOwner`() {
        val user =
            userRepository.save(
                User(
                    "owner-11@res.com",
                    "password",
                    "Hello",
                    "World",
                    UserRole.RESTAURANT_OWNER,
                ),
            )

        val request =
            RestaurantOwnerRequestDto(
                user.email,
                "Hello",
                "World",
            )

        assertThrows<IllegalArgumentException> {
            service.createRestaurantOwner(request)
        }
    }
}
