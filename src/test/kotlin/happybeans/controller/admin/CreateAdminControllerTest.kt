package happybeans.controller.admin

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.user.UserCreateRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.service.AdminAuthService
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateAdminControllerTest : AbstractRestDocsRestAssuredTest() {
    @Autowired lateinit var userRepository: UserRepository

    @Autowired lateinit var adminAuthService: AdminAuthService

    private lateinit var token: String

    @BeforeEach
    fun setUp() {
        val admin =
            userRepository.saveAndFlush(
                User(
                    email = "admin-owner@admin.com",
                    password = "12345678",
                    firstName = "first",
                    lastName = "last",
                    role = UserRole.ADMIN,
                ),
            )
        token = adminAuthService.login(LoginRequestDto(admin.email, "12345678"))
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun createAdmin() {
        val request =
            UserCreateRequestDto(
                email = "admin-2@admin.com",
                password = "12345678",
                firstName = "first",
                lastName = "last",
            )

        val response =
            given().log().all()
                .header("Authorization", "Bearer $token")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .filter(
                    document(
                        "admin-create-admin",
                        requestHeaders(
                            headerWithName("Authorization").description("Bearer access token"),
                        ),
                        requestFields(
                            fieldWithPath("email")
                                .type(JsonFieldType.STRING)
                                .description("Admin email (valid email, non-blank)"),
                            fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .description("Password (min 6 chars)"),
                            fieldWithPath("firstName")
                                .type(JsonFieldType.STRING)
                                .description("First name (min 3 chars)"),
                            fieldWithPath("lastName")
                                .type(JsonFieldType.STRING)
                                .description("Last name (min 3 chars)"),
                        ),
                        responseFields(
                            fieldWithPath("message")
                                .type(JsonFieldType.STRING)
                                .description("Creation status message (e.g. 'Admin created!')"),
                        ),
                    ),
                )
                .`when`().post("/api/admin/create-admin")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
    }
}
