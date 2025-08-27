package happybeans.controller.member

import happybeans.controller.AbstractRestDocsMockMvcTest
import happybeans.dto.order.OrderListResponse
import happybeans.dto.order.OrderProductResponse
import happybeans.dto.order.OrderResponse
import happybeans.enums.OrderStatus
import happybeans.service.MemberOrderService
import happybeans.service.OrderPaymentService
import happybeans.utils.exception.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse
import org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.relaxedResponseFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime

class MemberOrderControllerTest : AbstractRestDocsMockMvcTest() {
    @MockitoBean
    lateinit var memberOrderService: MemberOrderService

    @MockitoBean
    lateinit var orderPaymentService: OrderPaymentService

    @Test
    fun `GET order by id - OK & docs`() {
        val orderId = 1L
        val orderResponse =
            OrderResponse(
                orderId = orderId,
                createdAt = LocalDateTime.now(),
                status = OrderStatus.PENDING,
                totalAmount = 25.50,
                paymentId = "pi_123456789",
                products =
                    listOf(
                        OrderProductResponse(
                            price = 12.75,
                            quantity = 2,
                            imageUrl = "https://cdn.example.png",
                        ),
                    ),
            )

        whenever(memberOrderService.getOrderById(orderId)).thenReturn(orderResponse)

        mockMvc.get("/api/member/orders/{orderId}", orderId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "member-orders-get-by-id",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("orderId").description("Order ID"),
                        ),
                        responseFields(
                            fieldWithPath("orderId")
                                .type(JsonFieldType.NUMBER)
                                .description("Unique order ID"),
                            fieldWithPath("createdAt")
                                .type(JsonFieldType.STRING)
                                .description("Order creation timestamp (ISO-8601)"),
                            fieldWithPath("status")
                                .type(JsonFieldType.STRING)
                                .description("Order status (e.g. PENDING, COMPLETED, REJECTED)"),
                            fieldWithPath("totalAmount")
                                .type(JsonFieldType.NUMBER)
                                .description("Total order amount"),
                            fieldWithPath("paymentId")
                                .type(JsonFieldType.STRING)
                                .description("External payment identifier"),
                            fieldWithPath("products")
                                .type(JsonFieldType.ARRAY)
                                .description("List of ordered products"),
                            fieldWithPath("products[].price")
                                .type(JsonFieldType.NUMBER)
                                .description("Price per unit"),
                            fieldWithPath("products[].quantity")
                                .type(JsonFieldType.NUMBER)
                                .description("Quantity ordered"),
                            fieldWithPath("products[].imageUrl")
                                .type(JsonFieldType.STRING)
                                .optional()
                                .description("Image URL (nullable)"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `GET order by id - not found - 404 & docs`() {
        val orderId = 42L
        whenever(memberOrderService.getOrderById(orderId))
            .thenThrow(EntityNotFoundException("Order with id $orderId not found"))

        mockMvc.get("/api/member/orders/{orderId}", orderId) {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isNotFound() }
            }
            .andDo {
                handle(
                    document(
                        "member-orders-get-by-id-404",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("orderId").description("Order ID"),
                        ),
                        relaxedResponseFields(
                            fieldWithPath("message").type(JsonFieldType.STRING).description("Error message"),
                            fieldWithPath("errorCode").optional().type(JsonFieldType.STRING)
                                .description("Short error code (optional)"),
                            fieldWithPath("requestId").optional().type(JsonFieldType.STRING)
                                .description("Correlation id (optional)"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `POST cart-checkout - returns paymentUrl & docs`() {
        val paymentUrl = "https://pay.example/checkout-123"
        whenever(orderPaymentService.handleCartCheckout(testUser))
            .thenReturn(paymentUrl)

        mockMvc.post("/api/member/orders/cart-checkout") {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "member-orders-cart-checkout",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("paymentUrl").type(JsonFieldType.STRING)
                                .description("Redirect URL to payment checkout"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `POST buy-dish - returns paymentUrl & docs`() {
        val dishOptionId = 7L
        val paymentUrl = "https://pay.example/checkout-xyz"
        whenever(orderPaymentService.handleBuyDish(testUser, dishOptionId))
            .thenReturn(paymentUrl)

        mockMvc.post("/api/member/orders/buy-dish/{dishOptionId}", dishOptionId) {
            accept = MediaType.APPLICATION_JSON
            contentType = MediaType.APPLICATION_JSON
        }
            .andExpect {
                status { isOk() }
            }
            .andDo {
                handle(
                    document(
                        "member-orders-buy-dish",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                            parameterWithName("dishOptionId").description("Dish option ID"),
                        ),
                        responseFields(
                            fieldWithPath("paymentUrl").type(JsonFieldType.STRING)
                                .description("Redirect URL to payment checkout"),
                        ),
                    ),
                )
            }
    }

    @Test
    fun `get all orders - returns orders`() {
        val sample =
            OrderResponse(
                orderId = 123L,
                createdAt = LocalDateTime.now(),
                status = OrderStatus.PENDING,
                totalAmount = 10.0,
                paymentId = "pi_test",
                products =
                    listOf(
                        OrderProductResponse(price = 10.0, quantity = 1, imageUrl = null),
                    ),
            )

        whenever(memberOrderService.getAllUserOrders(testUser.id))
            .thenReturn(OrderListResponse(listOf(sample)))

        mockMvc.get("/api/member/orders") {
            accept = MediaType.APPLICATION_JSON
        }
            .andExpect { status { isOk() } }
            .andDo {
                handle(
                    document(
                        "member-orders-list",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("orders").type(JsonFieldType.ARRAY).description("List of orders"),
                            fieldWithPath("orders[].orderId").type(JsonFieldType.NUMBER).description("Order ID"),
                            fieldWithPath("orders[].createdAt").type(JsonFieldType.STRING).description("Created at (ISO-8601)"),
                            fieldWithPath("orders[].status").type(JsonFieldType.STRING).description("Order status"),
                            fieldWithPath("orders[].totalAmount").type(JsonFieldType.NUMBER).description("Total amount"),
                            fieldWithPath("orders[].paymentId").type(JsonFieldType.STRING).description("Payment identifier"),
                            fieldWithPath("orders[].products").type(JsonFieldType.ARRAY).description("Products in the order"),
                            fieldWithPath("orders[].products[].price").type(JsonFieldType.NUMBER).description("Price per unit"),
                            fieldWithPath("orders[].products[].quantity").type(JsonFieldType.NUMBER).description("Quantity"),
                            fieldWithPath("orders[].products[].imageUrl").type(JsonFieldType.STRING).optional()
                                .description("Image URL (nullable)"),
                        ),
                    ),
                )
            }
    }
}
