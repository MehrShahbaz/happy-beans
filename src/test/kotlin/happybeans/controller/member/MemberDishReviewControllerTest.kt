package happybeans.controller.member

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.service.DishReviewService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MemberDishReviewControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var dishReviewService: DishReviewService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("POST /api/member/review/dishOption -> 201 and MessageResponse")
    fun createDishReview_ok() {
        val body = mapOf("rating" to 4.5, "message" to "This pasta was amazing!", "entityId" to 1L)
        whenever(dishReviewService.createDishReview(any(), any())).thenReturn(1L)

        mockMvc.perform(
            post("/api/member/review/dishOption")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(header().string("Location", "1"))
            .andExpect(jsonPath("$.message").value("Dish review created"))
            .andDo(
                document(
                    "dish-review-create tap",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    relaxedRequestFields(
                        fieldWithPath("rating").type(JsonFieldType.NUMBER).description("Rating of the dish"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Review message"),
                        fieldWithPath("entityId").type(JsonFieldType.NUMBER).description("ID of the dish option"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )

        verify(dishReviewService).createDishReview(eq(testUser), any())
    }

    @Test
    @DisplayName("POST /api/member/review/dishOption -> 400 when invalid rating")
    fun createDishReview_invalidRating() {
        val body = mapOf("rating" to 6.0, "message" to "Invalid rating!", "entityId" to 1L)

        mockMvc.perform(
            post("/api/member/review/dishOption")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isBadRequest)
            .andDo(
                document(
                    "dish-review-create-invalid-rating",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    relaxedRequestFields(
                        fieldWithPath("rating").type(JsonFieldType.NUMBER)
                            .description("Invalid rating (must be between 0 and 5)"),
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Review message"),
                        fieldWithPath("entityId").type(JsonFieldType.NUMBER).description("ID of the dish option"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("PATCH /api/member/review/dishOption/{dishReviewId} -> 200 and MessageResponse")
    fun updateDishReview_ok() {
        val dishReviewId = 1L
        val body = mapOf("message" to "Updated review: pasta was good!")

        mockMvc.perform(
            patch("/api/member/review/dishOption/{dishReviewId}", dishReviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Updated dish Review"))
            .andDo(
                document(
                    "dish-review-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishReviewId").description("ID of the dish review"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Updated review message"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )

        verify(dishReviewService).updateDishReview(eq(dishReviewId), any(), eq(testUser))
    }

    @Test
    @DisplayName("GET /api/member/review/dishOption -> 200 and empty list when no reviews")
    fun getDishReviewsByUserId_empty() {
        whenever(dishReviewService.getReviewsByUserId(any())).thenReturn(emptyList())

        mockMvc.perform(
            get("/api/member/review/dishOption")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
            .andDo(
                document(
                    "dish-review-get-by-user-empty",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("Empty list of dish reviews"),
                    ),
                ),
            )

        verify(dishReviewService).getReviewsByUserId(eq(testUser.id))
    }

    @Test
    @DisplayName("GET /api/member/review/dishOption/{dishOptionId} -> 200 and empty list when no reviews")
    fun getDishReviewsByDishOptionId_empty() {
        val dishOptionId = 1L
        whenever(dishReviewService.getReviewsByDishOptionId(any())).thenReturn(emptyList())

        mockMvc.perform(
            get("/api/member/review/dishOption/by-dish-option/{dishOptionId}", dishOptionId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
            .andDo(
                document(
                    "dish-review-get-by-dish-option-empty",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishOptionId").description("ID of the dish option"),
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("Empty list of dish reviews"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("GET /api/member/review/dishOption/by-dish/{dishId} -> 200 and empty list when no reviews")
    fun getDishReviewsByDishId_empty() {
        val dishId = 1L
        whenever(dishReviewService.getReviewsByDishId(any())).thenReturn(emptyList())

        mockMvc.perform(
            get("/api/member/review/dishOption/by-dish/{dishId}", dishId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$.length()").value(0))
            .andDo(
                document(
                    "dish-review-get-by-dish-empty",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishId").description("ID of the dish"),
                    ),
                    responseFields(
                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("Empty list of dish reviews"),
                    ),
                ),
            )

        verify(dishReviewService).getReviewsByDishId(eq(dishId))
    }

    @Test
    @DisplayName("DELETE /api/member/review/dishOption/{dishReviewId} -> 204 when review deleted")
    fun deleteDishReviewById_ok() {
        val dishReviewId = 1L

        mockMvc.perform(
            delete("/api/member/review/dishOption/{dishReviewId}", dishReviewId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "dish-review-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishReviewId").description("ID of the dish review to delete"),
                    ),
                ),
            )

        verify(dishReviewService).deleteReview(eq(dishReviewId))
    }
}
