package happybeans.controller.admin

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.config.interceptor.AdminInterceptor
import happybeans.dto.auth.AuthTokenPayload
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
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.Optional

@WebMvcTest(controllers = [AdminRestaurantController::class])
@AutoConfigureRestDocs(
    uriScheme = "https",
    uriHost = "happy-beans.shop",
    uriPort = 8080,
)
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
}
