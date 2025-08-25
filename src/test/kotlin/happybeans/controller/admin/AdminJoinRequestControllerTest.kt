package happybeans.controller.admin

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.model.JoinRequest
import happybeans.model.User
import happybeans.repository.JoinRequestRepository
import happybeans.repository.UserRepository
import happybeans.service.AdminAuthService
import happybeans.service.EmailDispatchService
import io.restassured.http.ContentType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.bean.override.mockito.MockitoBean

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminJoinRequestControllerTest : AbstractRestDocsRestAssuredTest() {
    @Autowired private lateinit var userRepository: UserRepository

    @Autowired private lateinit var joinRequestRepository: JoinRequestRepository

    @Autowired private lateinit var adminAuthService: AdminAuthService

    @MockitoBean lateinit var emailDispatchService: EmailDispatchService

    private lateinit var token: String

    @BeforeEach
    fun setUp() {
        joinRequestRepository.saveAndFlush(
            JoinRequest(
                email = "test@test.com",
                firstName = "test",
                lastName = "test",
                message = "Please approve my request",
            ),
        )

        val admin =
            userRepository.saveAndFlush(
                User(
                    email = "admin-owner@admin.com",
                    password = "password",
                    firstName = "first",
                    lastName = "last",
                    role = UserRole.ADMIN,
                ),
            )

        token = adminAuthService.login(LoginRequestDto(admin.email, admin.password))
        doNothing()
            .whenever(emailDispatchService)
            .sendRestaurantOwnerWelcomeEmail(any(), any(), any())
    }

    @AfterEach
    fun tearDown() {
        joinRequestRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun getAllRequests() {
        val response =
            given().log().all()
                .header("Authorization", "Bearer $token")
                .accept(ContentType.JSON)
                .filter(
                    document(
                        "admin-join-request-get-all",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName("Authorization").description("Bearer access token"),
                        ),
                        responseFields(
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Join request ID"),
                            fieldWithPath("[].email").type(JsonFieldType.STRING).description("Applicant email"),
                            fieldWithPath("[].firstName").type(JsonFieldType.STRING).description("Applicant first name"),
                            fieldWithPath("[].lastName").type(JsonFieldType.STRING).description("Applicant last name"),
                            fieldWithPath("[].message").type(JsonFieldType.STRING).description("Application message"),
                            fieldWithPath("[].status").type(JsonFieldType.STRING).description("Request status (PENDING/...)"),
                            fieldWithPath("[].createdAt").type(JsonFieldType.STRING).optional()
                                .description("Creation timestamp"),
                        ),
                    ),
                )
                .`when`().get("/api/admin/join-request")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun acceptInvite() {
        val id = joinRequestRepository.findAll().first().id

        val response =
            given().log().all()
                .header("Authorization", "Bearer $token")
                .accept(ContentType.JSON)
                .filter(
                    document(
                        "admin-join-request-accept",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName("Authorization").description("Bearer access token"),
                        ),
                        pathParameters(
                            parameterWithName("joinRequestId").description("Join request ID to accept"),
                        ),
                        responseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING).description("Confirmation message"),
                        ),
                    ),
                )
                .`when`().post("/api/admin/join-request/accept/{joinRequestId}", id)
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }

    @Test
    fun rejectInvite() {
        val id = joinRequestRepository.findAll().first().id

        val response =
            given().log().all()
                .header("Authorization", "Bearer $token")
                .accept(ContentType.JSON)
                .filter(
                    document(
                        "admin-join-request-reject",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestHeaders(
                            headerWithName("Authorization").description("Bearer access token"),
                        ),
                        pathParameters(
                            parameterWithName("joinRequestId").description("Join request ID"),
                        ),
                        responseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING).description("Confirmation message"),
                        ),
                    ),
                )
                .`when`().post("/api/admin/join-request/reject/{joinRequestId}", id)
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
