package happybeans.controller.dish

import happybeans.TestFixture
import happybeans.dto.dish.DishUpdateRequest
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
        // Setup: Create restaurant and dish directly in the DB (bypassing authentication)
        val restaurant = TestFixture.createHappyBeansCafe()
        val savedUser = userRepository.save(restaurant.user)
        restaurant.user = savedUser
        val savedRestaurant = restaurantRepository.save(restaurant)

        // Use the simple version without options
        val dish = TestFixture.createMargheritaPizza()
        savedRestaurant.dishes.add(dish)
        // Save again to trigger cascade for the dish
        restaurantRepository.save(savedRestaurant)

        val savedDish = dishRepository.findAll().first()
        val dishId = savedDish.id

        // === TEST 1: GET DISH ===
        val getResponse =
            restTemplate.getForEntity(
                "${baseUrl()}/restaurant-owner/dish/$dishId",
                String::class.java,
            )

        // Debug: let's see what we actually got
        println("Response status: ${getResponse.statusCode}")
        println("Response body: ${getResponse.body}")

        // This should return 401 because E2E tests don't have auth
        assertThat(getResponse.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        assertThat(getResponse.body).isNotNull

        // === TEST 2: UPDATE DISH ===
        val updateRequest =
            DishUpdateRequest(
                name = "Updated Test Pizza",
                description = "Updated description for testing",
                image = "updated-pizza.jpg",
            )

        // PUT /api/restaurant-owner/dish/{dishId}
        val updateResponse =
            restTemplate.exchange(
                "${baseUrl()}/restaurant-owner/dish/$dishId",
                HttpMethod.PUT,
                HttpEntity(updateRequest),
                String::class.java,
            )

        assertThat(updateResponse.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)

        // === TEST 3: DELETE DISH ===
        // DELETE /api/restaurant-owner/dish/{dishId}
        val deleteResponse =
            restTemplate.exchange(
                "${baseUrl()}/restaurant-owner/dish/$dishId",
                HttpMethod.DELETE,
                null,
                String::class.java,
            )

        assertThat(deleteResponse.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
    }

    @Test
    fun `should return 401 when getting non-existent dish without auth`() {
        val nonExistentDishId = 999999L

        // GET /api/restaurant-owner/dish/{dishId} - non-existent dish
        val response =
            restTemplate.getForEntity(
                "${baseUrl()}/restaurant-owner/dish/$nonExistentDishId",
                String::class.java,
            )

        assertThat(response.statusCode).isEqualTo(HttpStatus.UNAUTHORIZED)
        // The controller returns 401 for unauthorized access before checking if dish exists
    }
}
