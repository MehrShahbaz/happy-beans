package happybeans.service

import happybeans.dto.restaurant.RestaurantCreateRequest
import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.dto.restaurant.WorkingDateHourRequest
import happybeans.model.Restaurant
import happybeans.model.User
import happybeans.repository.RestaurantRepository
import happybeans.repository.UserRepository
import happybeans.utils.exception.DuplicateEntityException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.DayOfWeek
import java.time.LocalTime

@SpringBootTest
class RestaurantServiceTest {
    @Autowired
    lateinit var service: RestaurantService

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var restaurantRepository: RestaurantRepository

    lateinit var user: User
    lateinit var request: RestaurantCreateRequest
    lateinit var restaurant: Restaurant

    @BeforeEach
    fun setUp() {
        user =
            userRepository.save(
                User(
                    "owner@res.com",
                    "123456789",
                    "Res",
                    "Owner",
                ),
            )

        val workingDateHours =
            DayOfWeek.entries.map {
                WorkingDateHourRequest(
                    it,
                    LocalTime.now(),
                    LocalTime.now(),
                )
            }

        request =
            RestaurantCreateRequest(
                "name",
                "description",
                "image",
                "address",
                workingDateHours,
            )

        restaurant = service.createRestaurant(request, user)
    }

    @AfterEach
    fun tearDown() {
        restaurantRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun createRestaurant() {
        assertThat(restaurant.id).isNotNull()
    }

    @Test
    fun `throws if same name createRestaurant`() {
        assertThrows<DuplicateEntityException> {
            service.createRestaurant(request, user)
        }
    }

    @Test
    fun patchRestaurant() {
        val patch =
            RestaurantPatchRequest(
                name = "change",
                description = "change description",
            )

        service.patchRestaurant(
            patch,
            restaurant.id,
            user.id,
        )

        val res = restaurantRepository.findById(restaurant.id).orElse(null)

        assertThat(res.name).isEqualTo(patch.name)
        assertThat(res.description).isEqualTo(patch.description)
    }

    @Test
    fun deleteRestaurant() {
        service.deleteRestaurant(restaurant.id, user.id)
        val res = restaurantRepository.findById(restaurant.id).orElse(null)
        assertThat(res).isNull()
    }

    @Test
    fun getAllOwnedRestaurants() {
        val res = service.getAllOwnedRestaurants(user.id)

        assertThat(res).hasSize(1)
    }

    @Test
    fun getRestaurantByIdAndOwnerId() {
        val res = service.getRestaurantByIdAndOwnerId(restaurant.id, user.id)

        assertThat(res).isNotNull()
    }
}
