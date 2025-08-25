package happybeans.controller.admin

import happybeans.TestFixture
import happybeans.enums.RestaurantStatus
import happybeans.repository.RestaurantRepository
import happybeans.service.AdminRestaurantService
import happybeans.service.RestaurantService
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
@Transactional
class AdminRestaurantE2ETest(
    @Autowired private val restaurantService: RestaurantService,
    @Autowired private val restaurantRepository: RestaurantRepository,
    @Autowired private val adminRestaurantService: AdminRestaurantService,
) {
    @Test
    fun getAllRestaurants() {
        assertDoesNotThrow {
            adminRestaurantService.getAllRestaurants()
        }
    }

    @Test
    fun `get all restaurants returns created restaurant`() {
        val restaurantOwner = TestFixture.createRestaurantOwner()
        val request = TestFixture.createRestaurantCreateRequest()
        val restaurant = restaurantService.createRestaurant(request, restaurantOwner)

        val allRestaurants = adminRestaurantService.getAllRestaurants()

        assertThat(allRestaurants).extracting<Long> { it.id }
            .contains(restaurant.id)
    }

    @Test
    fun `create and delete restaurant`() {
        // given: a restaurant owner + create request
        val restaurantOwner = TestFixture.createRestaurantOwner()
        val request = TestFixture.createRestaurantCreateRequest()

        // when: create the restaurant
        val restaurant = restaurantService.createRestaurant(request, restaurantOwner)

        // then: the restaurant exists in DB
        assertThat(restaurantRepository.findById(restaurant.id)).isPresent

        // when: delete restaurant
        adminRestaurantService.deleteRestaurant(restaurant.id)

        // then: restaurant removed
        assertThat(restaurantRepository.findById(restaurant.id)).isEmpty
    }

    @Test
    fun `deactivate restaurant status`() {
        // given: a restaurant owner + create request
        val restaurantOwner = TestFixture.createRestaurantOwner()
        val request = TestFixture.createRestaurantCreateRequest()

        // when: create the restaurant
        val restaurant = restaurantService.createRestaurant(request, restaurantOwner)
        assertThat(restaurant.status).isEqualTo(RestaurantStatus.ACTIVE)

        // when & then: restaurant status is updated
        adminRestaurantService.updateRestaurantStatus(restaurant.id, RestaurantStatus.INACTIVE)
        assertThat(restaurant.status).isEqualTo(RestaurantStatus.INACTIVE)
    }
}
