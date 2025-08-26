package happybeans.controller.member

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.review.RestaurantReviewDto
import happybeans.service.RestaurantReviewService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class MemberRestaurantReviewControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var restaurantReviewService: RestaurantReviewService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("POST /api/member/review/restaurant -> 201 Created and MessageResponse")
    fun createRestaurantReview_created() {
        val newId = 123L
        whenever(restaurantReviewService.createRestaurantReview(eq(testUser), any())).thenReturn(newId)

        val body =
            mapOf(
                "restaurantId" to 77,
                "rating" to 5,
                "comment" to "Tasty!",
            )

        mockMvc.perform(
            post("/api/member/review/restaurant")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", newId.toString()))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Restaurant review created successfully"))
            .andDo(
                document(
                    "member-review-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    relaxedRequestFields(
                        fieldWithPath("restaurantId").type(JsonFieldType.NUMBER).description("Restaurant ID"),
                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("Rating value"),
                        fieldWithPath("comment").type(JsonFieldType.STRING).optional().description("Optional comment"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("PATCH /api/member/review/restaurant/{restaurantReviewId} -> 200 and MessageResponse")
    fun updateRestaurantReview_ok() {
        val reviewId = 456L
        doNothing().whenever(restaurantReviewService)
            .updateRestaurantReview(eq(reviewId), any(), eq(testUser))

        val body =
            mapOf(
                "message" to "Still good",
            )

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/member/review/restaurant/{restaurantReviewId}", reviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Updated Restaurant Review"))
            .andDo(
                document(
                    "member-review-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantReviewId").description("Restaurant Review ID"),
                    ),
                    // В DTO есть только message
                    relaxedRequestFields(
                        fieldWithPath("message")
                            .type(JsonFieldType.STRING)
                            .description("New review message"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("GET /api/member/review/restaurant/{restaurantId} -> 200 and List<RestaurantReviewDto>")
    fun getReviewsByRestaurantId_ok() {
        val restaurantId = 77L

        val createdAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0)
        val updatedAt = LocalDateTime.of(2025, 1, 2, 12, 0, 0)

        whenever(restaurantReviewService.getReviewsByRestaurantId(eq(restaurantId))).thenReturn(
            listOf(
                RestaurantReviewDto(
                    id = 1L,
                    userName = "Test User",
                    rating = 5.0,
                    message = "Great!",
                    restaurantId = restaurantId,
                    restaurantName = "Pasta Place",
                    createdAt = createdAt,
                    updatedAt = updatedAt,
                ),
            ),
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/review/restaurant/{restaurantId}", restaurantId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].userName").value("Test User"))
            .andExpect(jsonPath("$[0].rating").value(5.0))
            .andExpect(jsonPath("$[0].message").value("Great!"))
            .andExpect(jsonPath("$[0].restaurantId").value(restaurantId.toInt()))
            .andExpect(jsonPath("$[0].restaurantName").value("Pasta Place"))
            .andExpect(jsonPath("$[0].createdAt").value("2025-01-01T10:00:00"))
            .andExpect(jsonPath("$[0].updatedAt").value("2025-01-02T12:00:00"))
            .andDo(
                document(
                    "member-review-get-by-restaurant",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantId").description("Restaurant ID"),
                    ),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Review ID"),
                        fieldWithPath("[].userName").type(JsonFieldType.STRING).description("Author name"),
                        fieldWithPath("[].rating").type(JsonFieldType.NUMBER).description("Rating value (1–5)"),
                        fieldWithPath("[].message").type(JsonFieldType.STRING).description("Review message"),
                        fieldWithPath("[].restaurantId").type(JsonFieldType.NUMBER).description("Restaurant ID"),
                        fieldWithPath("[].restaurantName").type(JsonFieldType.STRING).optional()
                            .description("Restaurant name (if available)"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING)
                            .description("Creation time (ISO-8601)"),
                        fieldWithPath("[].updatedAt").type(JsonFieldType.STRING)
                            .description("Last update time (ISO-8601)"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("GET /api/member/review/restaurant/user/{userId} -> 200 and List<RestaurantReviewDto>")
    fun getReviewsByUserId_ok() {
        val userIdInPath = 999L
        whenever(restaurantReviewService.getReviewsByUserId(eq(testUser.id))).thenReturn(emptyList())

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/review/restaurant/user/{userId}", userIdInPath)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "member-review-get-by-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("userId").description("User ID in path (ignored; controller uses logged-in user)"),
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("List of reviews (may be empty)"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("DELETE /api/member/review/restaurant/{restaurantId} -> 204 No Content")
    fun deleteRestaurantReviewById_noContent() {
        val reviewId = 77L // параметр называется restaurantId в контроллере, но фактически это reviewId
        doNothing().whenever(restaurantReviewService).deleteReviewById(eq(reviewId))

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/member/review/restaurant/{restaurantId}", reviewId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "member-review-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantId").description("Restaurant Review ID"),
                    ),
                ),
            )
    }
}
