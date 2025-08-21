//package happybeans.controller.dish
//
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import happybeans.controller.member.AbstractDocumentTest
//import happybeans.model.Dish
//import happybeans.service.DishService
//import org.junit.jupiter.api.DisplayName
//import org.junit.jupiter.api.Test
//import org.mockito.kotlin.any
//import org.mockito.kotlin.whenever
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.http.MediaType
//import org.springframework.http.ResponseEntity
//import org.springframework.mock.http.server.reactive.MockServerHttpRequest.get
//import org.springframework.test.context.bean.override.mockito.MockitoBean
//import org.springframework.test.web.servlet.MockMvc
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
//
//class DishControllerTest() : AbstractDocumentTest() {
//    @MockitoBean
//    private lateinit var dishService: DishService
//
//    private val objectMapper = jacksonObjectMapper()
//
//    @Test
//    @DisplayName("GET /api/member/cart -> 200 and list of cart products")
//    fun getDishBy_id_ok() {
//        whenever(dishService.getDishById(any()))
//            .thenReturn(
//                ResponseEntity<Dish>
//        )
//
//        mockMvc.perform(
//            get("/api/member/cart")
//                .accept(MediaType.APPLICATION_JSON),
//        )
//            .andExpect(status().isOk)
//            .andDo(
//
//            )
//    }
//
//}