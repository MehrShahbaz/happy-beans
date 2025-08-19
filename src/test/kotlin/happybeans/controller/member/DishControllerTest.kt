package happybeans.controller.dish

import com.fasterxml.jackson.databind.ObjectMapper
import happybeans.dto.dish.DishCreateRequest
import happybeans.model.Dish
import happybeans.service.DishService
import happybeans.utils.exception.EntityNotFoundException
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@WebMvcTest(controllers = [DishController::class])
class DishControllerTest {
    @TestConfiguration
    class TestConfig : WebMvcConfigurer

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var dishService: DishService

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `GET dish by id should return 200 with dish data`() {
        // Given
        val dishId = 1L
        val dish = createTestDish(dishId, "Margherita Pizza")
        given(dishService.findById(dishId)).willReturn(dish)

        // When & Then
        mockMvc.perform(get("/api/dish/{dishId}", dishId))
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(dishId))
            .andExpect(jsonPath("$.name").value("Margherita Pizza"))
    }

    @Test
    fun `GET dish by id should return 404 when dish not found`() {
        // Given
        val dishId = 999L
        given(dishService.findById(dishId)).willThrow(EntityNotFoundException("Dish not found"))

        // When & Then
        mockMvc.perform(get("/api/dish/{dishId}", dishId))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `GET dishes by restaurant should return 200 with dish list`() {
        // Given
        val restaurantId = 1L
        val pageable = PageRequest.of(0, 10)
        val dishes =
            listOf(
                createTestDish(1L, "Margherita Pizza"),
                createTestDish(2L, "Caesar Salad"),
            )
        given(dishService.findByRestaurantId(restaurantId, pageable)).willReturn(dishes)

        // When & Then
        mockMvc.perform(
            get("/api/dish/restaurant/{restaurantId}", restaurantId)
                .param("page", "0")
                .param("size", "10"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].name").value("Margherita Pizza"))
            .andExpect(jsonPath("$[1].name").value("Caesar Salad"))
    }

    @Test
    fun `GET dishes by restaurant should return 200 with empty list when no dishes`() {
        // Given
        val restaurantId = 1L
        val pageable = PageRequest.of(0, 10)
        given(dishService.findByRestaurantId(restaurantId, pageable)).willReturn(emptyList())

        // When & Then
        mockMvc.perform(
            get("/api/dish/restaurant/{restaurantId}", restaurantId)
                .param("page", "0")
                .param("size", "10"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(0))
    }

    @Test
    fun `POST create dish should return 201 with location header`() {
        // Given
        val restaurantId = 1L
        val dishCreateRequest =
            DishCreateRequest(
                name = "New Pizza",
                description = "Delicious pizza",
                image = "pizza.jpg",
                dishOptionRequests = mutableSetOf(),
            )
        val createdDish = createTestDish(5L, "New Pizza")
        given(dishService.createDish(restaurantId, dishCreateRequest)).willReturn(createdDish)

        // When & Then
        mockMvc.perform(
            post("/api/dish/restaurant/{restaurantId}", restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dishCreateRequest)),
        )
            .andExpect(status().isCreated)
            .andExpect(header().string("Location", "/api/dish/restaurant/$restaurantId/5"))
            .andExpect(jsonPath("$.message").value("Product added to cart"))
    }

    @Test
    fun `POST create dish should return 400 when validation fails`() {
        // Given
        val restaurantId = 1L
        val invalidRequest = mapOf("name" to "") // Empty name

        // When & Then
        mockMvc.perform(
            post("/api/dish/restaurant/{restaurantId}", restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST create dish should return 404 when restaurant not found`() {
        // Given
        val restaurantId = 999L
        val dishCreateRequest =
            DishCreateRequest(
                name = "New Pizza",
                description = "Delicious pizza",
                image = "pizza.jpg",
                dishOptionRequests = mutableSetOf(),
            )
        given(dishService.createDish(restaurantId, dishCreateRequest))
            .willThrow(EntityNotFoundException("Restaurant not found"))

        // When & Then
        mockMvc.perform(
            post("/api/dish/restaurant/{restaurantId}", restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dishCreateRequest)),
        )
            .andExpect(status().isNotFound)
    }

    private fun createTestDish(
        id: Long,
        name: String,
    ): Dish {
        return Dish(
            name = name,
            description = "Test description",
            image = "test.jpg",
            dishOptions = mutableSetOf(),
            id = id,
        )
    }
}
