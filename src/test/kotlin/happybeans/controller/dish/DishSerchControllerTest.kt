package happybeans.controller.dish

import happybeans.TestFixture
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.model.User
import happybeans.service.DishService
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DishSearchControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var dishService: DishService

    @Test
    @DisplayName("get filtered dishes by user tags succssful")
    fun getFilteredDishesByUser_Success() {
        val dishes = listOf(TestFixture.createMargheritaPizzaWithAllOptions())
        
        whenever(dishService.getFilteredDishesByUser(any<User>())).thenReturn(dishes)

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/dishes/search/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray)
            .andDo(
                document(
                    "dish-search-filtered-by-user",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("Dish ID"),
                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("Dish name"),
                        fieldWithPath("[].description").type(JsonFieldType.STRING).description("Dish description"),
                        fieldWithPath("[].image").type(JsonFieldType.STRING).description("Dish image URL"),
                        fieldWithPath("[].dishOptions").type(JsonFieldType.ARRAY).description("Dish options list"),
                        fieldWithPath("[].dishOptions[].id").type(JsonFieldType.NUMBER).description("Option ID"),
                        fieldWithPath("[].dishOptions[].name").type(JsonFieldType.STRING).description("Option name"),
                        fieldWithPath("[].dishOptions[].description").type(JsonFieldType.STRING).description("Option description").optional(),
                        fieldWithPath("[].dishOptions[].price").type(JsonFieldType.NUMBER).description("Option price"),
                        fieldWithPath("[].dishOptions[].image").type(JsonFieldType.STRING).description("Option image URL"),
                        fieldWithPath("[].dishOptions[].available").type(JsonFieldType.BOOLEAN).description("Option availability status"),
                        fieldWithPath("[].dishOptions[].prepTimeMinutes").type(JsonFieldType.NUMBER).description("Preparation time in minutes")
                    )
                )
            )

        verify(dishService).getFilteredDishesByUser(any<User>())
    }
}