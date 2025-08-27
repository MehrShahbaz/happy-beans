package happybeans.controller.guest

import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.dish.DishResponse
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.service.DishService
import happybeans.service.RestaurantService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import java.time.LocalDateTime

class GuestDishControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    lateinit var dishService: DishService

    @MockitoBean
    lateinit var restaurantService: RestaurantService

    @Test
    fun `GET guest dishes by restaurant - returns DishResponse list & docs`() {
        val restaurantId = 10L

        val dish =
            DishResponse(
                id = 11L,
                name = "Pho Bo",
                description = "Vietnamese beef noodle soup",
                image = "https://cdn.example/pho-bo.jpg",
                restaurantId = restaurantId,
                dishOptions = emptySet(),
            )
        whenever(dishService.getAllDishes()).thenReturn(listOf(dish))

        mockMvc.get("/api/guest/{restaurantId}/dishes", restaurantId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "guest-dishes-by-restaurant-list",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("restaurantId").description("Restaurant ID"),
                        ),
                        responseFields(
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("List of dishes"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).optional()
                                .description("Dish ID (nullable)"),
                            fieldWithPath("[].name").type(JsonFieldType.STRING).description("Dish name"),
                            fieldWithPath("[].description").type(JsonFieldType.STRING).description("Dish description"),
                            fieldWithPath("[].image").type(JsonFieldType.STRING).description("Image URL"),
                            fieldWithPath("[].restaurantId").type(JsonFieldType.NUMBER).optional()
                                .description("Restaurant ID (nullable)"),
                            fieldWithPath("[].dishOptions").type(JsonFieldType.ARRAY)
                                .description("Dish options (elements omitted)"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `GET guest restaurants - returns Restaurant list & docs`() {
        val owner =
            User(
                id = 2L,
                firstName = "Owner",
                lastName = "User",
                email = "owner@example.com",
                password = "secret",
            )

        val restaurant =
            Restaurant(
                user = owner,
                name = "Happy Beans",
                description = "Fresh & tasty bowls",
                image = "https://cdn.example/hb-logo.png",
                addressUrl = "https://maps.example/hb",
                workingDateHours = mutableListOf(),
                dishes = mutableListOf(),
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
                id = 5L,
            )

        whenever(restaurantService.getALlRestaurants()).thenReturn(listOf(restaurant))

        mockMvc.get("/api/guest/restaurant") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "guest-restaurants-list",
                        preprocessResponse(prettyPrint()),
                        relaxedResponseFields(
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("List of restaurants"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Restaurant ID"),
                            fieldWithPath("[].name").type(JsonFieldType.STRING).description("Restaurant name"),
                            fieldWithPath("[].description").type(JsonFieldType.STRING).description("Description"),
                            fieldWithPath("[].image").type(JsonFieldType.STRING).description("Image URL"),
                            fieldWithPath("[].addressUrl").type(JsonFieldType.STRING).description("Google Maps URL"),
                            fieldWithPath("[].workingDateHours").type(JsonFieldType.ARRAY)
                                .optional().description("Working hours (elements omitted)"),
                            fieldWithPath("[].dishes").type(JsonFieldType.ARRAY)
                                .optional().description("Menu dishes (elements omitted)"),
                            fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                                .optional().description("Created at (ISO-8601)"),
                            fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                                .optional().description("Updated at (ISO-8601)"),
                            fieldWithPath("[].user").optional()
                                .description("Owner entity (if serialized; usually hidden)"),
                        ),
                    ),
                )
            }
    }
}
