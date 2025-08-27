package happybeans.controller.admin

import happybeans.config.interceptor.AdminInterceptor
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.user.RestaurantOwnerRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.service.EmailDispatchService
import happybeans.service.HandleRestaurantOwnerCreateService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation.headerWithName
import org.springframework.restdocs.headers.HeaderDocumentation.responseHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseBody
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.post

class CreateRestaurantOwnerControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    lateinit var handleRestaurantOwnerCreateService: HandleRestaurantOwnerCreateService

    @MockitoBean lateinit var emailDispatchService: EmailDispatchService

    @MockitoBean private lateinit var adminInterceptor: AdminInterceptor

    @Test
    fun `POST create restaurant owner - 201 Created & docs`() {
        val request =
            RestaurantOwnerRequestDto(
                email = "new.owner@example.com",
                firstName = "Alice",
                lastName = "Doe",
            )
        val createdUser =
            User(
                id = 123L,
                firstName = request.firstName,
                lastName = request.lastName,
                email = request.email,
                password = "tmp",
                role = UserRole.ADMIN,
            )

        whenever(adminInterceptor.preHandle(any(), any(), any())).thenReturn(true)
        doNothing()
            .whenever(emailDispatchService)
            .sendRestaurantOwnerWelcomeEmail(any(), any(), any())
        whenever(handleRestaurantOwnerCreateService.handleCreateRestaurantOwner(any()))
            .thenReturn(createdUser)

        mockMvc.post("/api/admin/restaurant-owner") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content =
                """
                {
                  "email": "${request.email}",
                  "firstName": "${request.firstName}",
                  "lastName": "${request.lastName}"
                }
                """.trimIndent()
        }
            .andExpect { status { isCreated() } }
            .andExpect { header { exists("Location") } }
            .andExpect { content { json("""{"message":"RestaurantOwner created!"}""") } }
            .andDo {
                handle(
                    document(
                        "admin-restaurant-owner-create",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("Owner email (valid email format)"),
                            fieldWithPath("firstName").type(JsonFieldType.STRING)
                                .description("Owner first name (min 3 chars)"),
                            fieldWithPath("lastName").type(JsonFieldType.STRING)
                                .description("Owner last name"),
                        ),
                        responseHeaders(
                            headerWithName("Location")
                                .description("URI of the created owner resource"),
                        ),
                        responseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING)
                                .description("Operation result message"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `POST create restaurant owner - 400 on invalid request & docs`() {
        val badJson =
            """
            {
              "email": "not-an-email",
              "firstName": "Al",
              "lastName": "Doe"
            }
            """.trimIndent()

        whenever(adminInterceptor.preHandle(any(), any(), any())).thenReturn(true)
        doNothing()
            .whenever(emailDispatchService)
            .sendRestaurantOwnerWelcomeEmail(any(), any(), any())

        mockMvc.post("/api/admin/restaurant-owner") {
            contentType = MediaType.APPLICATION_JSON
            accept = MediaType.APPLICATION_JSON
            content = badJson
        }
            .andExpect { status { isBadRequest() } }
            .andDo {
                handle(
                    document(
                        "admin-restaurant-owner-create-400",
                        preprocessResponse(prettyPrint()),
                        requestFields(
                            fieldWithPath("email").type(JsonFieldType.STRING)
                                .description("Owner email (must be valid)"),
                            fieldWithPath("firstName").type(JsonFieldType.STRING)
                                .description("Owner first name (min 3 chars)"),
                            fieldWithPath("lastName").type(JsonFieldType.STRING)
                                .description("Owner last name"),
                        ),
                        responseBody(),
                    ),
                )
            }
    }
}
