package happybeans.controller.dish

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.TestFixture
import happybeans.config.argumentResolver.RestaurantOwnerArgumentResolver
import happybeans.config.interceptor.RestaurantOwnerInterceptor
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.dish.DishOptionPatchRequest
import happybeans.dto.dish.DishOptionUpdateRequest
import happybeans.enums.UserRole
import happybeans.model.Tag
import happybeans.model.User
import happybeans.service.DishService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
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
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedRequestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DishOptionControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var dishService: DishService

    @MockitoBean
    private lateinit var restaurantOwnerArgumentResolver: RestaurantOwnerArgumentResolver

    @MockitoBean
    private lateinit var restaurantOwnerInterceptor: RestaurantOwnerInterceptor

    private val objectMapper = jacksonObjectMapper()

    private val testOwner =
        User(
            email = "test@owner.com",
            password = "password",
            firstName = "Test",
            lastName = "Owner",
            role = UserRole.RESTAURANT_OWNER,
        ).apply { id = 1L }

    @BeforeEach
    fun setUpRestaurantOwnerAuth() {
        whenever(restaurantOwnerArgumentResolver.supportsParameter(any())).thenReturn(true)
        whenever(restaurantOwnerArgumentResolver.resolveArgument(any(), any(), any(), any()))
            .thenReturn(testOwner)
        whenever(restaurantOwnerInterceptor.preHandle(any(), any(), any())).thenReturn(true)
    }

    @Test
    @DisplayName("PUT /api/restaurant-owner/dish-options/{optionId} -> 200 and success message")
    fun updateDishOption_ok() {
        val optionId = 1L
        val updateRequest =
            DishOptionUpdateRequest(
                name = "Updated Personal Margherita (8\")",
                description = "Updated perfect size for one person",
                price = 13.99,
                image = "https://updated-image.com/pizza.jpg",
                prepTimeMinutes = 20,
            )

        val updatedDishOption = TestFixture.createMargheritaPizzaWithAllOptions().dishOptions.first()
        whenever(dishService.updateDishOption(eq(optionId), any(), any())).thenReturn(updatedDishOption)

        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/restaurant-owner/dish-options/{optionId}", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Option updated"))
            .andDo(
                document(
                    "dish-option-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option to update"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("New dish option name"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("New dish option description"),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("New dish option price"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("New dish option image URL"),
                        fieldWithPath("prepTimeMinutes").type(JsonFieldType.NUMBER).description("New preparation time in minutes"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).updateDishOption(eq(optionId), any(), any())
    }

    @Test
    @DisplayName("PATCH /api/restaurant-owner/dish-options/{optionId} -> 200 and success message")
    fun patchDishOption_ok() {
        val optionId = 1L
        val patchRequest =
            DishOptionPatchRequest(
                price = 14.99,
                prepTimeMinutes = 25,
            )

        val patchedDishOption = TestFixture.createMargheritaPizzaWithAllOptions().dishOptions.first()
        whenever(dishService.patchDishOption(eq(optionId), any(), any())).thenReturn(patchedDishOption)

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/restaurant-owner/dish-options/{optionId}", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patchRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Option updated"))
            .andDo(
                document(
                    "dish-option-patch",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option to patch"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Dish option name to update").optional(),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("Dish option description to update").optional(),
                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("Dish option price to update").optional(),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("Dish option image URL to update").optional(),
                        fieldWithPath("prepTimeMinutes").type(JsonFieldType.NUMBER).description("Preparation time to update").optional(),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).patchDishOption(eq(optionId), any(), any())
    }

    @Test
    @DisplayName("DELETE /api/restaurant-owner/dish-options/{optionId} -> 204 No Content")
    fun deleteDishOption_ok() {
        val optionId = 1L

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/restaurant-owner/dish-options/{optionId}", optionId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "dish-option-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option to delete"),
                    ),
                ),
            )

        verify(dishService).deleteDishOption(eq(optionId), any())
    }

    // Tag endpoint tests
    @Test
    @DisplayName("GET /api/restaurant-owner/dish-options/{optionId}/tags -> 200 and tags")
    fun getDishOptionTags_ok() {
        // Given
        val optionId = 1L
        val tags =
            setOf(
                Tag("spicy", id = 1L),
                Tag("vegetarian", id = 2L),
            )
        whenever(dishService.getDishOptionTags(optionId)).thenReturn(tags)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/restaurant-owner/dish-options/{optionId}/tags", optionId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andDo(
                document(
                    "dish-option-tags-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option"),
                    ),
                ),
            )

        verify(dishService).getDishOptionTags(optionId)
    }

    @Test
    @DisplayName("POST /api/restaurant-owner/dish-options/{optionId}/tags -> 200 and success message")
    fun addDishOptionTag_ok() {
        // Given
        val optionId = 1L
        val tagRequest = mapOf("tagName" to "spicy")
        val dishOption = TestFixture.createMargheritaPizzaWithAllOptions().dishOptions.first()

        whenever(dishService.addDishOptionTag(eq(optionId), eq("spicy"), any())).thenReturn(dishOption)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/restaurant-owner/dish-options/{optionId}/tags", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag added successfully"))
            .andDo(
                document(
                    "dish-option-tags-add",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("tagName").type(JsonFieldType.STRING).description("Name of the tag to add"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).addDishOptionTag(eq(optionId), eq("spicy"), any())
    }

    @Test
    @DisplayName("DELETE /api/restaurant-owner/dish-options/{optionId}/tags -> 200 and success message")
    fun removeDishOptionTag_ok() {
        // Given
        val optionId = 1L
        val tagRequest = mapOf("tagName" to "spicy")
        val dishOption = TestFixture.createMargheritaPizzaWithAllOptions().dishOptions.first()

        whenever(dishService.removeDishOptionTag(eq(optionId), eq("spicy"), any())).thenReturn(dishOption)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/restaurant-owner/dish-options/{optionId}/tags", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tag removed successfully"))
            .andDo(
                document(
                    "dish-option-tags-remove",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("tagName").type(JsonFieldType.STRING).description("Name of the tag to remove"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).removeDishOptionTag(eq(optionId), eq("spicy"), any())
    }

    @Test
    @DisplayName("PUT /api/restaurant-owner/dish-options/{optionId}/tags -> 200 and success message")
    fun updateDishOptionTags_ok() {
        // Given
        val optionId = 1L
        val tagRequest = mapOf("tagNames" to setOf("spicy", "vegetarian", "italian"))
        val dishOption = TestFixture.createMargheritaPizzaWithAllOptions().dishOptions.first()

        whenever(dishService.updateDishOptionTags(eq(optionId), any(), any())).thenReturn(dishOption)

        // When & Then
        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/restaurant-owner/dish-options/{optionId}/tags", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Tags updated successfully"))
            .andDo(
                document(
                    "dish-option-tags-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("optionId").description("ID of the dish option"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("tagNames").type(JsonFieldType.ARRAY).description("Set of tag names to replace current tags"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).updateDishOptionTags(eq(optionId), eq(setOf("spicy", "vegetarian", "italian")), any())
    }
}
