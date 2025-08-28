package happybeans.controller.admin

import happybeans.controller.AbstractRestDocsRestAssuredTest
import happybeans.dto.auth.LoginRequestDto
import happybeans.enums.UserRole
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.repository.RestaurantRepository
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
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AdminRestaurantControllerTest : AbstractRestDocsRestAssuredTest() {
    @Autowired lateinit var restaurantRepository: RestaurantRepository

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

        restaurantRepository.saveAndFlush(
            Restaurant(
                user = admin,
                name = "Test Resto",
                description = "description",
                image = "image-url",
                addressUrl = "https://maps.example/resto",
            ),
        )
    }

    @AfterEach
    fun tearDown() {
        restaurantRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun getAllRestaurants() {
        val response =
            given().log().all()
                .header("Authorization", "Bearer $token")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
//                .filter(
//                    document(
//                        "admin-get-restaurants",
//                        preprocessResponse(prettyPrint()),
//                        requestHeaders(
//                            headerWithName("Authorization").description("Bearer access token"),
//                        ),
//                        relaxedResponseFields(
//                            fieldWithPath("[]")
//                                .type(JsonFieldType.ARRAY)
//                                .description("List of restaurants"),
//                            fieldWithPath("[].id")
//                                .type(JsonFieldType.NUMBER)
//                                .description("Restaurant owner"),
//                            fieldWithPath("[].name")
//                                .type(JsonFieldType.STRING)
//                                .description("Restaurant name"),
//                            fieldWithPath("[].description")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Restaurant description (optional)"),
//                            fieldWithPath("[].image")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Image URL (optional)"),
//                            fieldWithPath("[].addressUrl")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Address/Map URL (optional)"),
//                            fieldWithPath("[].workingDateHours")
//                                .type(JsonFieldType.ARRAY)
//                                .optional()
//                                .description("Working hours per day"),
//                            fieldWithPath("[].workingDateHours[].dayOfWeek")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Day of week (e.g. MONDAY)"),
//                            fieldWithPath("[].workingDateHours[].openTime")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Opening time (HH:mm)"),
//                            fieldWithPath("[].workingDateHours[].closeTime")
//                                .type(JsonFieldType.STRING)
//                                .optional()
//                                .description("Closing time (HH:mm)"),
//                            fieldWithPath("[].dishes")
//                                .type(JsonFieldType.ARRAY)
//                                .optional()
//                                .description("List of dishes"),
//                            fieldWithPath("[].createdAt")
//                                .type(JsonFieldType.STRING)
//                                .description("Creation timestamp"),
//                            fieldWithPath("[].updatedAt")
//                                .type(JsonFieldType.STRING)
//                                .description("Last update timestamp"),
//                        ),
//                    ),
//                )
                .`when`().get("/api/admin/restaurants")
                .then().log().all()
                .extract()

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value())
    }
}
