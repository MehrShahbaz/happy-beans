package happybeans.service

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AdminRestaurantServiceTest {
    @Autowired
    lateinit var adminRestaurantService: AdminRestaurantService

    @Test
    fun getAllRestaurants() {
        assertDoesNotThrow {
            adminRestaurantService.getAllRestaurants()
        }
    }
}
