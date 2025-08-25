package happybeans.controller.restaurant

import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.RestaurantRepository
import happybeans.repository.TagRepository
import happybeans.repository.UserRepository
import happybeans.service.LoginService
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestaurantControllerTest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired private lateinit var tagRepository: TagRepository

    @Autowired private lateinit var loginService: LoginService

    @Autowired private lateinit var userRepository: UserRepository

    @Autowired private lateinit var restaurantRepository: RestaurantRepository

    private lateinit var token: String

    @BeforeEach
    fun setup() {
        val user =
            userRepository.saveAndFlush(
                User(
                    email = "owner@test.com",
                    password = "123456789",
                    firstName = "Test",
                    lastName = "Owner",
                    role = UserRole.RESTAURANT_OWNER,
                ),
            )

        token = loginService.login(LoginRequestDto(user.email, user.password), UserRole.RESTAURANT_OWNER)
    }

    @AfterEach
    fun cleanup() {
        tagRepository.deleteAll()
        restaurantRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun createRestaurant() {
        val body =
            RestaurantCreateRequest(
                name = "name",
                description = "description",
                image = "image",
                addressUrl = "addressUrl",
            )

        val response =
            RestAssured
                .given().port(port).log().all()
                .header("Authorization", "Bearer $token")
                .contentType(ContentType.JSON)
                .body(body)
                .`when`().post("/api/restaurant-owner/restaurants")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }

    @Test
    fun getAllRestaurants() {
        val response =
            RestAssured
                .given().port(port).log().all()
                .header("Authorization", "Bearer $token")
                .accept(ContentType.JSON)
                .`when`().get("/api/restaurant-owner/restaurants")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
