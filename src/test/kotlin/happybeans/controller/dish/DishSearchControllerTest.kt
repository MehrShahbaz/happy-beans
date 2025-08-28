package happybeans.controller.dish

import happybeans.TestFixture
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.service.DishService
import happybeans.service.UserService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
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
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DishSearchControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var dishService: DishService

    @MockitoBean
    private lateinit var userService: UserService

    @Test
    @DisplayName("get filtered dishes by user tags successful")
    fun getFilteredDishOptionsByUser_Success() {
        // Given
        val dish = TestFixture.createPizzaWithAllOptions()
        val filteredDishOptions = dish.dishOptions.toList().take(2)

        val userLikes = setOf("spicy", "vegetarian")
        val userDislikes = setOf("peanuts")

        whenever(userService.getUserLikes(any())).thenReturn(
            userLikes.map { happybeans.model.Tag(it) }.toSet(),
        )
        whenever(userService.getUserDislikes(any())).thenReturn(
            userDislikes.map { happybeans.model.Tag(it) }.toSet(),
        )
        whenever(dishService.getFilteredDishOptionsByUserTags(userLikes, userDislikes)).thenReturn(filteredDishOptions)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/dishes/search")
                .contentType(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "dish-search-filtered-by-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Dish option ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("Dish option name"),
                        fieldWithPath("[].description").type(JsonFieldType.STRING).description("Dish option description").optional(),
                        fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("Dish option price"),
                        fieldWithPath("[].image").type(JsonFieldType.STRING).description("Dish option image URL"),
                        fieldWithPath("[].available").type(JsonFieldType.BOOLEAN).description("Dish option availability"),
                        fieldWithPath("[].prepTimeMinutes").type(JsonFieldType.NUMBER).description("Preparation time in minutes"),
                        fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("Creation timestamp").optional(),
                        fieldWithPath("[].updatedAt").type(JsonFieldType.STRING).description("Last update timestamp").optional(),
                    ),
                ),
            )

        verify(userService).getUserLikes(any())
        verify(userService).getUserDislikes(any())
        verify(dishService).getFilteredDishOptionsByUserTags(userLikes, userDislikes)
    }
}
