package happybeans.controller.member

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.auth.LoginRequestDto
import happybeans.dto.user.UserCreateRequestDto
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document

@AutoConfigureRestDocs
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberAuthControllerTest : AbstractRestDocsRestAssuredTest() {
    @Test
    fun `sign-up User`() {
        val user =
            UserCreateRequestDto(
                "temp-controlller@temp.com",
                "test-456",
                "First",
                "Last",
            )

        val response =
            RestAssured
                .given(spec).port(port).log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .filter(
                    document(
                        "member-auth-sign-up",
                        responseFields(
                            fieldWithPath("token")
                                .type(JsonFieldType.STRING)
                                .description("Authentication token"),
                        ),
                    ),
                )
                .`when`().post("/api/member/auth/sign-up")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()

        assertThat(response.body().jsonPath().get<String>("token")).isNotBlank()
    }

    @Test
    fun `login User`() {
        val email = "test-${System.nanoTime()}@ex.com"
        val password = "12345678"

        RestAssured.given(spec).port(port)
            .contentType(ContentType.JSON)
            .body(
                UserCreateRequestDto(
                    email = email,
                    password = password,
                    firstName = "John",
                    lastName = "Doe",
                ),
            )
            .`when`().post("/api/member/auth/sign-up")
            .then().statusCode(HttpStatus.CREATED.value())

        val response =
            RestAssured.given(spec).port(port).log().all()
                .contentType(ContentType.JSON)
                .body(LoginRequestDto(email, password))
                .filter(
                    document(
                        "member-auth-login",
                        responseFields(
                            fieldWithPath("token")
                                .type(JsonFieldType.STRING)
                                .description("Authentication token"),
                        ),
                    ),
                )
                .`when`().post("/api/member/auth/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()

        assertThat(response.body().jsonPath().get<String>("token")).isNotBlank()
    }
}
