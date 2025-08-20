package happybeans.model

import happybeans.dto.restaurant.RestaurantPatchRequest
import happybeans.dto.restaurant.WorkingDateHourRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.DayOfWeek
import java.time.LocalTime

class RestaurantTest {
    lateinit var restaurant: Restaurant
    lateinit var dish: Dish
    lateinit var user: User
    lateinit var workingDateHours: List<WorkingDateHourRequest>

    @BeforeEach
    fun init() {
        user =
            User(
                "user@user.com",
                "user12345",
                "user",
                "test",
            )
        restaurant =
            Restaurant(
                user,
                "test",
                "test",
                "test",
                "test",
            )
        dish =
            Dish(
                "test",
                "test",
                "test",
            )

        workingDateHours = DayOfWeek.entries.map {
            WorkingDateHourRequest(
                it,
                LocalTime.now(),
                LocalTime.now(),
            )
        }
    }

    @Test
    fun addDish() {
        restaurant.addDish(dish)
        assertThat(restaurant.dishes).contains(dish)
    }

    @Test
    fun patchFields() {
        val patch =
            RestaurantPatchRequest(
                name = "change",
                description = "change description",
                image = "change test",
                addressUrl = "change address url",
                workingDateHours
            )
        restaurant.patchFields(patch)

        assertThat(restaurant.name).isEqualTo(patch.name)
        assertThat(restaurant.description).isEqualTo(patch.description)
        assertThat(restaurant.image).isEqualTo(patch.image)
        assertThat(restaurant.addressUrl).isEqualTo(patch.addressUrl)
        assertThat(restaurant.workingDateHours.size).isEqualTo(restaurant.workingDateHours.size)
    }
}
