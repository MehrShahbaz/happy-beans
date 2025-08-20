package happybeans.controller.member

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.dto.cart.CartProductListResponse
import happybeans.dto.cart.CartProductResponse
import happybeans.service.CartProductService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.operation.preprocess.Preprocessors
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class CartProductControllerTest : AbstractDocumentTest() {
    @MockitoBean
    private lateinit var cartProductService: CartProductService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("GET /api/member/cart -> 200 and list of cart products")
    fun getCartProducts_empty_ok() {
        whenever(cartProductService.findAllByUserId2(any()))
            .thenReturn(
                CartProductListResponse(
                    listOf(
                        CartProductResponse("dishName", "dishOptionName", 1.0),
                    ),
                ),
            )

        mockMvc.perform(
            get("/api/member/cart")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.cartProducts").isArray)
            .andExpect(jsonPath("$.cartProducts.length()").value(1))
            .andDo(
                document(
                    "cart-get",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("cartProducts")
                            .type(JsonFieldType.ARRAY)
                            .description("List of dishes in cart"),
                        fieldWithPath("cartProducts[].dishName")
                            .type(JsonFieldType.STRING)
                            .description("Dish name in cart"),
                        fieldWithPath("cartProducts[].dishOptionName")
                            .type(JsonFieldType.STRING)
                            .description("Dish option name in cart"),
                        fieldWithPath("cartProducts[].dishPrice")
                            .type(JsonFieldType.NUMBER)
                            .description("Price"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("POST /api/member/cart/dish/{dishId}/dish-option/{dishOptionId} -> 200 and MessageResponse")
    fun addToCart_ok() {
        val dishId = 10L
        val optionId = 77L
        val body = mapOf("quantity" to 2)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post(
                "/api/member/cart/dish/{dishId}/dish-option/{dishOptionId}",
                dishId,
                optionId,
            )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Successfully added to cart!"))
            .andDo(
                document(
                    "cart-add",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    pathParameters(
                        parameterWithName("dishId").description("ID dish"),
                        parameterWithName("dishOptionId").description("ID dish option"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("quantity")
                            .type(JsonFieldType.NUMBER)
                            .description("Quantity of dish in cart"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )

        verify(cartProductService).addToCart(eq(testUser), eq(Pair(dishId, optionId)), any())
    }

    @Test
    @DisplayName("PATCH /api/member/cart/dish-option/{dishOptionId} -> 200 and MessageResponse")
    fun updateQuantity_ok() {
        val optionId = 77L
        val body = mapOf("quantity" to 5)

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/api/member/cart/dish-option/{dishOptionId}", optionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Option updated"))
            .andDo(
                document(
                    "cart-update",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    pathParameters(
                        parameterWithName("dishOptionId").description("ID опции блюда"),
                    ),
                    relaxedRequestFields(
                        fieldWithPath("quantity")
                            .type(JsonFieldType.NUMBER)
                            .description("New quantity in cart"),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("Message status"),
                    ),
                ),
            )

        verify(cartProductService).updateQuantity(eq(testUser), eq(optionId), any())
    }

    @Test
    @DisplayName("DELETE /api/member/cart/dish-option/{dishOptionId} -> 204")
    fun deleteOne_ok() {
        val optionId = 77L

        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/member/cart/dish-option/{dishOptionId}", optionId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "cart-delete-one",
                    preprocessRequest(Preprocessors.prettyPrint()),
                    preprocessResponse(Preprocessors.prettyPrint()),
                    pathParameters(
                        parameterWithName("dishOptionId").description("ID dish option, which should be deleted"),
                    ),
                ),
            )

        verify(cartProductService).deleteFromCart(testUser, optionId)
    }

    @Test
    @DisplayName("DELETE /api/member/cart -> 204, cart clear")
    fun clear_ok() {
        mockMvc.perform(
            RestDocumentationRequestBuilders.delete("/api/member/cart")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isNoContent)
            .andDo(
                document(
                    "cart-clear",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                ),
            )

        verify(cartProductService).clear(testUser)
    }
}
