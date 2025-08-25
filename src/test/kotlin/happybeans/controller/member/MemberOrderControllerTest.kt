package happybeans.controller.member

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.order.OrderIntentResponse
import happybeans.dto.order.OrderProductResponse
import happybeans.dto.order.OrderResponse
import happybeans.enums.OrderStatus
import happybeans.service.MemberOrderService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
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
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

class MemberOrderControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    private lateinit var memberOrderService: MemberOrderService

    private val objectMapper = jacksonObjectMapper()

    @Test
    @DisplayName("GET /api/member/orders -> 200 and OrderListResponse")
    fun getAllOrders_ok() {
        whenever(memberOrderService.getAllUserOrders(eq(testUser.id)))
            .thenReturn(emptyList())

        mockMvc.perform(
            get("/api/member/orders")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orders").isArray)
            .andDo(
                document(
                    "member-orders-get-all",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("orders")
                            .type(JsonFieldType.ARRAY)
                            .description("User orders"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("GET /api/member/orders/{orderId} -> 200 and OrderResponse")
    fun getOrderById_ok() {
        val orderId = 42L
        val createdAt = LocalDateTime.of(2025, 1, 1, 10, 0, 0)
        val status = OrderStatus.COMPLETED
        val total = 12.34
        val paymentId = "pi_123"
        val products =
            listOf(
                OrderProductResponse(
                    price = 5.5,
                    quantity = 2,
                    imageUrl = "http://example.com/img.png",
                ),
            )

        whenever(memberOrderService.getOrderById(eq(orderId)))
            .thenReturn(
                OrderResponse(
                    orderId = orderId,
                    createdAt = createdAt,
                    status = status,
                    totalAmount = total,
                    paymentId = paymentId,
                    products = products,
                ),
            )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/api/member/orders/{orderId}", orderId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.orderId").value(orderId))
            .andExpect(jsonPath("$.createdAt").value("2025-01-01T10:00:00"))
            .andExpect(jsonPath("$.status").value(status.name))
            .andExpect(jsonPath("$.totalAmount").value(total))
            .andExpect(jsonPath("$.paymentId").value(paymentId))
            .andExpect(jsonPath("$.products").isArray)
            .andExpect(jsonPath("$.products[0].price").value(5.5))
            .andExpect(jsonPath("$.products[0].quantity").value(2))
            .andExpect(jsonPath("$.products[0].imageUrl").value("http://example.com/img.png"))
            .andDo(
                document(
                    "member-orders-get-by-id",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("orderId").description("Order ID"),
                    ),
                    responseFields(
                        fieldWithPath("orderId")
                            .type(JsonFieldType.NUMBER)
                            .description("Order identifier"),
                        fieldWithPath("createdAt")
                            .type(JsonFieldType.STRING)
                            .description("Creation time (ISO-8601)"),
                        fieldWithPath("status")
                            .type(JsonFieldType.STRING)
                            .description("Order status"),
                        fieldWithPath("totalAmount")
                            .type(JsonFieldType.NUMBER)
                            .description("Total amount"),
                        fieldWithPath("paymentId")
                            .type(JsonFieldType.STRING)
                            .description("Payment identifier"),
                        fieldWithPath("products")
                            .type(JsonFieldType.ARRAY)
                            .description("Ordered products list"),
                        fieldWithPath("products[].price")
                            .type(JsonFieldType.NUMBER)
                            .description("Price of the ordered product"),
                        fieldWithPath("products[].quantity")
                            .type(JsonFieldType.NUMBER)
                            .description("Quantity ordered"),
                        fieldWithPath("products[].imageUrl")
                            .type(JsonFieldType.STRING)
                            .optional()
                            .description("Image URL of the product (if available)"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("POST /api/member/orders/cart-checkout -> 200 and MessageResponse")
    fun createCheckoutCartIntent_ok() {
        whenever(memberOrderService.createCheckoutCartIntent(eq(testUser)))
            .thenReturn(
                OrderIntentResponse(
                    intentId = "42L",
                    orderId = 42L,
                ),
            )

        mockMvc.perform(
            post("/api/member/orders/cart-checkout")
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("Cart Checkout"))
            .andDo(
                document(
                    "member-orders-cart-checkout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("message")
                            .type(JsonFieldType.STRING)
                            .description("Message status"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("POST /api/member/orders/confirm-checkout/{orderId} -> 200 and MessageResponse")
    fun confirmCheckout_ok() {
        val orderId = 123L
        whenever(memberOrderService.createCheckoutCartIntent(eq(testUser)))
            .thenReturn(
                OrderIntentResponse(
                    intentId = "42L",
                    orderId = 42L,
                ),
            )

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/member/orders/confirm-checkout/{orderId}", orderId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("confirm-checkout/$orderId"))
            .andDo(
                document(
                    "member-orders-confirm-checkout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("orderId").description("Order ID to confirm checkout"),
                    ),
                    responseFields(
                        fieldWithPath("message")
                            .type(JsonFieldType.STRING)
                            .description("Message status"),
                    ),
                ),
            )
    }

    @Test
    @DisplayName("POST /api/member/orders/buy-dish/{dishOptionId} -> 200 and MessageResponse")
    fun buyProduct_ok() {
        val dishOptionId = 777L
        doNothing().whenever(memberOrderService).buyProduct(eq(testUser), eq(dishOptionId))

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/api/member/orders/buy-dish/{dishOptionId}", dishOptionId)
                .accept(MediaType.APPLICATION_JSON),
        )
            .andExpect(status().isOk)
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("buy-product/$dishOptionId"))
            .andDo(
                document(
                    "member-orders-buy-dish",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("dishOptionId").description("Dish option ID to buy"),
                    ),
                    responseFields(
                        fieldWithPath("message")
                            .type(JsonFieldType.STRING)
                            .description("Message status"),
                    ),
                ),
            )
    }
}
