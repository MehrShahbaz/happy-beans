package happybeans.controller.admin

import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.service.AdminAuthService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class CreateRestaurantOwnerControllerTest {
    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var adminAuthService: AdminAuthService

    private lateinit var token: String
    private lateinit var user: User

    @BeforeEach
    fun setUp() {
        user =
            userRepository.save(
                User(
                    "admin-owner@admin.com",
                    "12345678",
                    "first",
                    "last",
                    UserRole.ADMIN,
                ),
            )
        token = adminAuthService.login(LoginRequestDto(user.email, user.password))
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun createRestaurantOwner() {
        val actual =
            RestaurantOwnerRequestDto(
                "mehrshahbaz7@gmail.com",
                "Hello",
                "World",
            )

        val response =
            RestAssured
                .given().log().all()
                .body(actual)
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().post("/api/admin/restaurant-owner")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }
}
