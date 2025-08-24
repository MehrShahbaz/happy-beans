package happybeans.controller.admin

import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.model.JoinRequest
import happybeans.model.User
import happybeans.repository.JoinRequestRepository
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
class AdminJoinRequestControllerTest {
    private lateinit var token: String
    private lateinit var user: User
    private lateinit var joinRequest: JoinRequest

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    lateinit var adminAuthService: AdminAuthService

    @Autowired
    lateinit var joinRequestRepository: JoinRequestRepository

    @BeforeEach
    fun setUp() {
        joinRequest =
            joinRequestRepository.save(
                JoinRequest(
                    "test@test.com",
                    "test",
                    "test",
                    "test",
                ),
            )
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
        joinRequestRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun getAllRequests() {
        val response =
            RestAssured
                .given().log().all()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .`when`().get("/api/admin/join-request")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
