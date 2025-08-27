package happybeans.controller.member

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.tag.TagRequest
import happybeans.model.Tag
import happybeans.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
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
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class MemberUserControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var userService: UserService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("GET /api/member/likes -> 200 and user's likes")
    fun getUserLikes_ok() {
        // Given
        val likes =
            setOf(
                Tag("spicy", id = 1L),
                Tag("vegetarian", id = 2L),
            )
        whenever(userService.getUserLikes(testUser.id)).thenReturn(likes)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/likes")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "member-get-likes",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Tag ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("Tag name"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("Tag creation time").optional(),
                    ),
                ),
            )

        verify(userService).getUserLikes(testUser.id)
    }

    @Test
    @DisplayName("POST /api/member/likes -> 200 and success message")
    fun addUserLike_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("spicy"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/member/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag added to likes successfully"))
            .andDo(
                document(
                    "member-add-like",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("tagNames").type(JsonFieldType.ARRAY).description("List of tag names to add to likes"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(userService).addUserLike(testUser.id, "spicy")
    }

    @Test
    @DisplayName("DELETE /api/member/likes -> 200 and success message")
    fun removeUserLike_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("spicy"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/member/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag removed from likes successfully"))
            .andDo(
                document(
                    "member-remove-like",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("tagNames").type(JsonFieldType.ARRAY).description("List of tag names to remove from likes"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(userService).removeUserLike(testUser.id, "spicy")
    }

    @Test
    @DisplayName("PUT /api/member/likes -> 200 and success message")
    fun updateUserLikes_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("spicy", "vegetarian", "italian"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/member/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Likes updated successfully"))
            .andDo(
                document(
                    "member-update-likes",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("tagNames").type(JsonFieldType.ARRAY).description("List of tag names to replace current likes"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(userService).updateUserLikes(eq(testUser.id), eq(setOf("spicy", "vegetarian", "italian")))
    }

    @Test
    @DisplayName("GET /api/member/dislikes -> 200 and user's dislikes")
    fun getUserDislikes_ok() {
        // Given
        val dislikes =
            setOf(
                Tag("meat", id = 1L),
                Tag("dairy", id = 2L),
            )
        whenever(userService.getUserDislikes(testUser.id)).thenReturn(dislikes)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/dislikes")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "member-get-dislikes",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Tag ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("Tag name"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("Tag creation time").optional(),
                    ),
                ),
            )

        verify(userService).getUserDislikes(testUser.id)
    }

    @Test
    @DisplayName("POST /api/member/likes with multiple tagNames -> 200 and success message")
    fun addMultipleUserLikes_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("spicy", "vegetarian"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/member/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tags added to likes successfully"))

        verify(userService).addUserLike(testUser.id, "spicy")
        verify(userService).addUserLike(testUser.id, "vegetarian")
    }

    @Test
    @DisplayName("POST /api/member/dislikes -> 200 and success message")
    fun addUserDislike_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("meat"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/member/dislikes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag added to dislikes successfully"))

        verify(userService).addUserDislike(testUser.id, "meat")
    }

    @Test
    @DisplayName("DELETE /api/member/dislikes -> 200 and success message")
    fun removeUserDislike_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("meat"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/member/dislikes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag removed from dislikes successfully"))

        verify(userService).removeUserDislike(testUser.id, "meat")
    }

    @Test
    @DisplayName("PUT /api/member/dislikes -> 200 and success message")
    fun updateUserDislikes_ok() {
        // Given
        val tagRequest = TagRequest(tagNames = listOf("meat", "dairy"))

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/member/dislikes")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Dislikes updated successfully"))

        verify(userService).updateUserDislikes(eq(testUser.id), eq(setOf("meat", "dairy")))
    }
}
