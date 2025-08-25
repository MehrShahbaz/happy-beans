package happybeans.controller.guest

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.joinRequest.JoinRequestDto
import happybeans.repository.JoinRequestRepository
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GuestJoinRequestControllerTest : AbstractRestDocsRestAssuredTest() {
    @Autowired
    lateinit var joinRequestRepository: JoinRequestRepository

    @AfterEach
    fun cleanUp() {
        joinRequestRepository.deleteAll()
    }

    @Test
    fun joinRequest_ok() {
        val request = createJoinRequestDto("test1@request.com")

        val response =
            given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .filter(
                    document(
                        "guest-join-request-create",
                        requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("Applicant email (valid & non-blank)"),
                            fieldWithPath("firstName").type(JsonFieldType.STRING)
                                .description("First name (min 3 chars)"),
                            fieldWithPath("lastName").type(JsonFieldType.STRING)
                                .description("Last name (min 3 chars)"),
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("Message (3..1000 chars)"),
                        ),
                        responseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("Status message, e.g. 'Request sent successfully'"),
                        ),
                    ),
                )
                .`when`().post("/api/guest/join-request")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
        assertThat(response.body().jsonPath().getString("message")).isEqualTo("Request sent successfully")
    }

    @Test
    fun joinRequest_conflict() {
        val request = createJoinRequestDto("test2@request.com")

        given().log().all()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body(request)
            .`when`().post("/api/guest/join-request")
            .then().log().all()

        val response =
            given().log().all()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request)
                .filter(
                    document(
                        "guest-join-request-create-conflict",
                        requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING).description("Applicant email"),
                            fieldWithPath("firstName").type(JsonFieldType.STRING).description("First name"),
                            fieldWithPath("lastName").type(JsonFieldType.STRING).description("Last name"),
                            fieldWithPath("message").type(JsonFieldType.STRING).description("Message"),
                        ),
                        relaxedResponseFields(
                            fieldWithPath("timestamp").type(JsonFieldType.STRING).description("Error time (ISO-8601)"),
                            fieldWithPath("status").type(JsonFieldType.NUMBER).description("HTTP status code (409)"),
                            fieldWithPath("error").type(JsonFieldType.STRING).description("Error title"),
                            fieldWithPath("path").type(JsonFieldType.STRING).description("Request path"),
                        ),
                    ),
                )
                .`when`().post("/api/guest/join-request")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value())
    }

    private fun createJoinRequestDto(email: String) =
        JoinRequestDto(
            email = email,
            firstName = "test",
            lastName = "test",
            message = "test message",
        )
}
