package happybeans.controller.member

import happybeans.dto.user.UserCreateRequestDto
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.specification.RequestSpecification
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@AutoConfigureRestDocs
@ExtendWith(SpringExtension::class, RestDocumentationExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberAuthControllerTest {
    @LocalServerPort
    private val port = 0

    private lateinit var spec: RequestSpecification

    @BeforeEach
    fun setUp(restDocumentation: RestDocumentationContextProvider) {
        // Set the base URI and port for RestAssured
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        this.spec =
            RequestSpecBuilder()
                .addFilter(documentationConfiguration(restDocumentation))
                .build()
    }

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
                .given(this.spec).log().all() // Use the pre-configured 'spec'
                .body(user)
                .contentType(ContentType.JSON)
                .filter(
                    document(
                        "sign-up-user",
                        responseFields(
                            fieldWithPath("token")
                                .type(JsonFieldType.STRING)
                                .description("Authentication token"),
                        ),
                    ),
                )
                .`when`().post("/api/member/auth/sign-up")
                .then().log().all().extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value())
        assertThat(response.body().jsonPath().get<String>("token")).isNotEmpty
    }
}
