package happybeans.controller.dish

import happybeans.TestFixture
import happybeans.dto.dish.DishResponse
import happybeans.dto.dish.DishUpdateRequest
import happybeans.dto.response.MessageResponse
import happybeans.repository.DishRepository
import happybeans.repository.RestaurantRepository
import happybeans.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(
    properties = [
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    ],
)
class DishE2ETest {
    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var restaurantRepository: RestaurantRepository

    @Autowired
    private lateinit var dishRepository: DishRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private fun baseUrl() = "http://localhost:$port/api"

    @Test
    fun `should perform GET and UPDATE operations on dishes`() {
        // Setup: Create restaurant and dish directly in database (bypassing authentication)
        val restaurant = TestFixture.createHappyBeansCafe()
        val savedUser = userRepository.save(restaurant.user) // Save user first
        restaurant.user = savedUser
        val savedRestaurant = restaurantRepository.save(restaurant)

        val dish = TestFixture.createMargheritaPizza() // Use simpler version without options initially
        savedRestaurant.dishes.add(dish)
        restaurantRepository.save(savedRestaurant) // Save again to trigger cascade for dish

        val savedDish = dishRepository.findAll().first()
        val dishId = savedDish.id

        // === TEST 1: GET DISH ===
        val getResponse =
            restTemplate.getForEntity(
                "${baseUrl()}/dish/$dishId",
                DishResponse::class.java,
            )

        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(getResponse.body).isNotNull
        assertThat(getResponse.body!!.id).isEqualTo(dishId)
        assertThat(getResponse.body!!.name).isEqualTo("Margherita Pizza")
        assertThat(getResponse.body!!.description).contains("Italian pizza")
        assertThat(getResponse.body!!.dishOptions).isEmpty() // Simple version has no options initially

        // === TEST 2: UPDATE DISH ===
        val updateRequest =
            DishUpdateRequest(
                name = "Updated Test Pizza",
                description = "Updated description for testing",
                image = "updated-pizza.jpg",
            )

        // PUT /api/dish/{dishId}
        val updateResponse =
            restTemplate.exchange(
                "${baseUrl()}/dish/$dishId",
                HttpMethod.PUT,
                HttpEntity(updateRequest),
                MessageResponse::class.java,
            )

        assertThat(updateResponse.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(updateResponse.body?.message).isEqualTo("Dish updated successfully")

        // Verify update in database
        val updatedDish = dishRepository.findById(dishId).orElse(null)
        assertThat(updatedDish).isNotNull
        assertThat(updatedDish!!.name).isEqualTo("Updated Test Pizza")
        assertThat(updatedDish.description).isEqualTo("Updated description for testing")

        // === TEST 3: DELETE DISH ===
        // DELETE /api/dish/{dishId}
        val deleteResponse =
            restTemplate.exchange(
                "${baseUrl()}/dish/$dishId",
                HttpMethod.DELETE,
                null,
                Void::class.java,
            )

        assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.NO_CONTENT)

        // Verify deletion in database
        val deletedDish = dishRepository.findById(dishId).orElse(null)
        assertThat(deletedDish).isNull()
    }

    @Test
    fun `should return 404 when getting non-existent dish`() {
        val nonExistentDishId = 999999L

        // GET /api/dish/{dishId} - non-existent dish
        val response =
            restTemplate.getForEntity(
                "${baseUrl()}/dish/$nonExistentDishId",
                String::class.java,
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        // The controller properly returns 404 for non-existent dishes
    }
}
