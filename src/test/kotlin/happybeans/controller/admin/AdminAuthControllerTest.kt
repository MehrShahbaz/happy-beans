package happybeans.controller.admin

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminAuthControllerTest : AbstractRestDocsRestAssuredTest() {
    @Autowired lateinit var userRepository: UserRepository

    private val rawPassword = "12345678"
    private lateinit var adminEmail: String

    @BeforeEach
    fun setUp() {
        adminEmail = "admin@login.com"
        userRepository.saveAndFlush(
            User(
                email = adminEmail,
                password = rawPassword,
                firstName = "Admin",
                lastName = "User",
                role = UserRole.ADMIN,
            ),
        )
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun login_ok() {
        val req = LoginRequestDto(email = adminEmail, password = rawPassword)

        val response =
            given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(req)
                .filter(
                    document(
                        "admin-auth-login",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("email")
                                .type(JsonFieldType.STRING)
                                .description("Admin email"),
                            fieldWithPath("password")
                                .type(JsonFieldType.STRING)
                                .description("Admin password"),
                        ),
                        responseFields(
                            fieldWithPath("token")
                                .type(JsonFieldType.STRING)
                                .description("JWT access token"),
                        ),
                    ),
                )
                .`when`().post("/api/admin/auth/login")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getString("token")).isNotBlank()
    }
}
