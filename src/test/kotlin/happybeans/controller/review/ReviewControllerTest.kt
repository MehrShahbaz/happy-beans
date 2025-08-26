package happybeans.controller.review

import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.review.DishReviewDto
import happybeans.dto.review.RestaurantReviewDto
import happybeans.service.DishReviewService
import happybeans.service.RestaurantReviewService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseBody
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime

class ReviewControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    lateinit var dishReviewService: DishReviewService

    @MockitoBean
    lateinit var restaurantReviewService: RestaurantReviewService

    @Test
    fun `GET dish reviews - returns list & docs`() {
        val sample =
            DishReviewDto(
                id = 11L,
                userName = "Alice",
                rating = 4.5,
                message = "Tasty!",
                dishOptionId = 7L,
                dishOptionName = "Large Pho",
                dishOptionPrice = 12.9,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now(),
            )
        whenever(dishReviewService.getAllReviews()).thenReturn(listOf(sample))

        mockMvc.get("/api/owner/reviews/dish") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("$").isArray }
            .andDo {
                handle(
                    document(
                        "owner-dish-reviews-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("List of dish reviews"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Review ID"),
                            fieldWithPath("[].userName").type(JsonFieldType.STRING).description("Author name"),
                            fieldWithPath("[].rating").type(JsonFieldType.NUMBER).description("Rating (1..5)"),
                            fieldWithPath("[].message").type(JsonFieldType.STRING).description("Review text"),
                            fieldWithPath("[].dishOptionId").type(JsonFieldType.NUMBER).description("Dish option ID"),
                            fieldWithPath("[].dishOptionName").type(JsonFieldType.STRING).optional()
                                .description("Dish option name (nullable)"),
                            fieldWithPath("[].dishOptionPrice").type(JsonFieldType.NUMBER).optional()
                                .description("Dish option price (nullable)"),
                            fieldWithPath("[].createdAt").type(JsonFieldType.STRING).optional()
                                .description("Created at (ISO-8601, nullable)"),
                            fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).optional()
                                .description("Updated at (ISO-8601, nullable)"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `GET dish average rating - returns number & docs`() {
        val dishOptionId = 7L
        whenever(dishReviewService.getAverageRatingForDishOption(dishOptionId)).thenReturn(4.2)

        mockMvc.get("/api/owner/reviews/dish/average-rating/{dishOptionId}", dishOptionId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "owner-dish-reviews-average-rating",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("dishOptionId").description("Dish option ID"),
                        ),
                        responseBody(),
                    ),
                )
            }
    }

    @Test
    fun `GET restaurant reviews - returns list & docs`() {
        val sample =
            RestaurantReviewDto(
                id = 21L,
                userName = "Bob",
                rating = 3.8,
                message = "Nice place",
                restaurantId = 5L,
                restaurantName = "Happy Beans",
                createdAt = LocalDateTime.now().minusDays(2),
                updatedAt = LocalDateTime.now(),
            )
        whenever(restaurantReviewService.getAllReviews()).thenReturn(listOf(sample))

        mockMvc.get("/api/owner/reviews/restaurant") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andExpect { jsonPath("$").isArray }
            .andDo {
                handle(
                    document(
                        "owner-restaurant-reviews-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("[]").type(JsonFieldType.ARRAY).description("List of restaurant reviews"),
                            fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Review ID"),
                            fieldWithPath("[].userName").type(JsonFieldType.STRING).description("Author name"),
                            fieldWithPath("[].rating").type(JsonFieldType.NUMBER).description("Rating (1..5)"),
                            fieldWithPath("[].message").type(JsonFieldType.STRING).description("Review text"),
                            fieldWithPath("[].restaurantId").type(JsonFieldType.NUMBER).description("Restaurant ID"),
                            fieldWithPath("[].restaurantName").type(JsonFieldType.STRING).optional()
                                .description("Restaurant name (nullable)"),
                            fieldWithPath("[].createdAt").type(JsonFieldType.STRING).optional()
                                .description("Created at (ISO-8601, nullable)"),
                            fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).optional()
                                .description("Updated at (ISO-8601, nullable)"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `GET restaurant average rating - returns number & docs`() {
        val restaurantId = 5L
        whenever(restaurantReviewService.getAverageRatingForRestaurant(restaurantId)).thenReturn(4.7)

        mockMvc.get("/api/owner/reviews/restaurant/average-rating/{restaurantId}", restaurantId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "owner-restaurant-reviews-average-rating",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("restaurantId").description("Restaurant ID"),
                        ),
                        responseBody(),
                    ),
                )
            }
    }
}
