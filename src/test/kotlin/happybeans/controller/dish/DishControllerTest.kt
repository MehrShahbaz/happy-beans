package happybeans.controller.dish

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.TestFixture
import happybeans.controller.member.AbstractDocumentTest
import happybeans.dto.dish.DishCreateRequest
import happybeans.dto.dish.DishOptionCreateRequest
import happybeans.dto.dish.DishUpdateRequest
import happybeans.service.DishService
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
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class DishControllerTest : AbstractDocumentTest() {
    @MockitoBean
    private lateinit var dishService: DishService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("GET /api/dish/{dishId} -> 200 and dish details")
    fun getDishById_ok() {
        val dishId = 1L
        val dish = TestFixture.createMargheritaPizzaWithAllOptions().apply { id = dishId }

        whenever(dishService.findById(dishId)).thenReturn(dish)

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/dish/{dishId}", dishId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dishId))
            .andExpect(jsonPath("$.name").value("Margherita Pizza"))
            .andDo(
                document(
                    "dish-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishId").description("ID of the dish to retrieve"),
                    ),
                    relaxedResponseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Dish ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Dish name"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("Dish description"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("Dish image URL"),
                        fieldWithPath("dishOptions").type(JsonFieldType.ARRAY).description("List of dish options"),
                        fieldWithPath("dishOptions[].id").type(JsonFieldType.NUMBER).description("Dish option ID"),
                        fieldWithPath("dishOptions[].name").type(JsonFieldType.STRING).description("Dish option name"),
                        fieldWithPath(
                            "dishOptions[].description",
                        ).type(JsonFieldType.STRING).description("Dish option description").optional(),
                        fieldWithPath("dishOptions[].price").type(JsonFieldType.NUMBER).description("Dish option price"),
                        fieldWithPath("dishOptions[].image").type(JsonFieldType.STRING).description("Dish option image URL"),
                        fieldWithPath("dishOptions[].available").type(JsonFieldType.BOOLEAN).description("Dish option availability"),
                        fieldWithPath(
                            "dishOptions[].prepTimeMinutes",
                        ).type(JsonFieldType.NUMBER).description("Preparation time in minutes"),
                        fieldWithPath("dishOptions[].rating").type(JsonFieldType.NUMBER).description("Dish option rating"),
                    ),
                ),
            )

        verify(dishService).findById(dishId)
    }

    @Test
    @DisplayName("POST /api/restaurant/{restaurantId}/dishes -> 201 and success message")
    fun createDish_ok() {
        val restaurantId = 1L
        val dishOption =
            DishOptionCreateRequest(
                name = "Personal Margherita (8\")",
                description = "Perfect size for one person with light appetite",
                price = 12.99,
                image = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80",
                available = true,
                rating = 4.7,
                prepTimeMinutes = 15,
            )

        val dishRequest =
            DishCreateRequest(
                name = "Margherita Pizza",
                description = "Classic Italian pizza with fresh tomato sauce, mozzarella di bufala, and aromatic basil leaves.",
                image = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=800&q=80",
                dishOptionRequests = mutableSetOf(dishOption),
            )

        val createdDish = TestFixture.createMargheritaPizza().apply { id = 1L }
        whenever(dishService.createDish(eq(restaurantId), any())).thenReturn(createdDish)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/restaurant/{restaurantId}/dishes", restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dishRequest)),
        )
            .andExpect(status().isCreated)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Dish created successfully"))
            .andDo(
                document(
                    "dish-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("restaurantId").description("ID of the restaurant to create dish in"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Dish name"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("Dish description"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("Dish image URL"),
                        fieldWithPath("dishOptionRequests").type(JsonFieldType.ARRAY).description("List of dish options to create"),
                        fieldWithPath("dishOptionRequests[].name").type(JsonFieldType.STRING).description("Dish option name"),
                        fieldWithPath(
                            "dishOptionRequests[].description",
                        ).type(JsonFieldType.STRING).description("Dish option description").optional(),
                        fieldWithPath("dishOptionRequests[].price").type(JsonFieldType.NUMBER).description("Dish option price"),
                        fieldWithPath("dishOptionRequests[].image").type(JsonFieldType.STRING).description("Dish option image URL"),
                        fieldWithPath(
                            "dishOptionRequests[].available",
                        ).type(JsonFieldType.BOOLEAN).description("Dish option availability").optional(),
                        fieldWithPath("dishOptionRequests[].rating").type(JsonFieldType.NUMBER).description("Dish option rating"),
                        fieldWithPath(
                            "dishOptionRequests[].prepTimeMinutes",
                        ).type(JsonFieldType.NUMBER).description("Preparation time in minutes"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).createDish(eq(restaurantId), any())
    }

    @Test
    @DisplayName("PUT /api/dish/{dishId} -> 200 and updated dish")
    fun updateDish_ok() {
        val dishId = 1L
        val updateRequest =
            DishUpdateRequest(
                name = "Updated Margherita Pizza",
                description = "Updated classic Italian pizza",
                image = "https://updated-image.com/pizza.jpg",
            )

        val updatedDish =
            TestFixture.createMargheritaPizza().apply {
                id = dishId
                name = "Updated Margherita Pizza"
                description = "Updated classic Italian pizza"
                image = "https://updated-image.com/pizza.jpg"
            }

        whenever(dishService.updateDish(eq(dishId), any())).thenReturn(updatedDish)

        mockMvc.perform(
            RestDocumentationRequestBuilders.put("/api/dish/{dishId}", dishId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dishId))
            .andExpect(jsonPath("$.name").value("Updated Margherita Pizza"))
            .andDo(
                document(
                    "dish-update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishId").description("ID of the dish to update"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("New dish name"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("New dish description"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("New dish image URL"),
                    ),
                    relaxedResponseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("Dish ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("Updated dish name"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("Updated dish description"),
                        fieldWithPath("image").type(JsonFieldType.STRING).description("Updated dish image URL"),
                        fieldWithPath("dishOptions").type(JsonFieldType.ARRAY).description("Dish options"),
                    ),
                ),
            )

        verify(dishService).updateDish(eq(dishId), any())
    }

    @Test
    @DisplayName("DELETE /api/dish/{dishId} -> 200 and success message")
    fun deleteDish_ok() {
        val dishId = 1L

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/dish/{dishId}", dishId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Dish deleted successfully"))
            .andDo(
                document(
                    "dish-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishId").description("ID of the dish to delete"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Success message"),
                    ),
                ),
            )

        verify(dishService).deleteDishById(dishId)
    }
}
