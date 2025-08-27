package happybeans.controller.admin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.config.interceptor.AdminInterceptor
import happybeans.dto.auth.AuthTokenPayload
import happybeans.dto.restaurant.RestaurantStatusUpdateRequest
import happybeans.enums.RestaurantStatus
import happybeans.enums.UserRole
import happybeans.infrastructure.JwtProvider
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.service.AdminRestaurantService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional

@WebMvcTest(controllers = [AdminRestaurantController::class])
@AutoConfigureRestDocs
class AdminRestaurantManageControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var adminRestaurantService: AdminRestaurantService

    @MockitoBean
    private lateinit var adminInterceptor: AdminInterceptor

    @MockitoBean
    private lateinit var jwtProvider: JwtProvider

    @MockitoBean
    private lateinit var userRepository: UserRepository

    private val objectMapper = jacksonObjectMapper()

    private val adminUser =
        User(
            email = "admin@test.com",
            password = "password",
            firstName = "Admin",
            lastName = "User",
            role = UserRole.ADMIN,
        ).apply { id = 1L }

    @BeforeEach
    fun setUpAdminAuth() {
        // Mock JWT validation
        doNothing().`when`(jwtProvider).validateToken(any())
        whenever(jwtProvider.getPayload(any())).thenReturn(
            AuthTokenPayload(adminUser.email),
        )

        // Mock user lookup
        whenever(userRepository.findByEmail(adminUser.email)).thenReturn(Optional.of(adminUser))

        // Mock interceptor to pass auth
        whenever(adminInterceptor.preHandle(any(), any(), any())).thenReturn(true)
    }

    @Test
    @DisplayName("DELETE /api/admin/restaurants/{restaurantId} -> 204 No Content")
    fun deleteRestaurant_ok() {
        val restaurantId = 123L

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/admin/restaurants/{restaurantId}", restaurantId)
                .header("Authorization", "Bearer mock-jwt-token")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "admin-restaurant-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantId").description("ID of the restaurant to delete"),
                    ),
                ),
            )

        verify(adminRestaurantService).deleteRestaurant(restaurantId)
    }

    @Test
    @DisplayName("PATCH /api/admin/restaurants/{restaurantId}/status -> 200 and success message")
    fun updateRestaurantStatus_ok() {
        val restaurantId = 123L
        val statusRequest = RestaurantStatusUpdateRequest(RestaurantStatus.SUSPENDED)

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/admin/restaurants/{restaurantId}/status", restaurantId)
                .header("Authorization", "Bearer mock-jwt-token")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Restaurant status is updated."))
            .andDo(
                document(
                    "admin-restaurant-status-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantId").description("ID of the restaurant to update"),
                    ),
                    requestFields(
                        fieldWithPath(
                            "status",
                        ).type(JsonFieldType.STRING).description("New restaurant status (ACTIVE, INACTIVE, SUSPENDED)"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(adminRestaurantService).updateRestaurantStatus(eq(restaurantId), eq(RestaurantStatus.SUSPENDED))
    }
}
