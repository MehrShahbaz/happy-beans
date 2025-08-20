package happybeans

import happybeans.enums.TagContainerType
import happybeans.model.Dish
import happybeans.model.DishOption
import happybeans.model.Restaurant
import happybeans.model.Tag
import happybeans.model.TagContainer
import happybeans.model.User
import happybeans.model.WorkingDateHour
import java.time.DayOfWeek
import java.time.LocalTime

object TestFixture {
    fun createMargheritaPizzaWithAllOptions(): Dish {
        val dish =
            Dish(
                name = "Margherita Pizza",
                description = "Classic Italian pizza with fresh tomato sauce, mozzarella di bufala, and aromatic basil leaves.",
                image = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=800&q=80",
            )

        val personalOption =
            DishOption(
                dish = dish,
                name = "Personal Margherita (8\")",
                description = "Perfect size for one person with light appetite",
                price = 12.99,
                image = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80",
                available = true,
                prepTimeMinutes = 15,
                rating = 4.7,
            )

        val mediumOption =
            DishOption(
                dish = dish,
                name = "Medium Margherita (12\")",
                description = "Great for sharing between 2-3 people",
                price = 18.99,
                image = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=600&q=80",
                available = true,
                prepTimeMinutes = 18,
                rating = 4.8,
            )

        val largeOption =
            DishOption(
                dish = dish,
                name = "Large Margherita (16\")",
                description = "Family size pizza perfect for 4-5 people",
                price = 24.99,
                image = "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=600&q=80",
                available = true,
                prepTimeMinutes = 22,
                rating = 4.9,
            )

        dish.addDishOptions(listOf(personalOption, mediumOption, largeOption))

        return dish
    }

    fun createMargheritaPizza() =
        Dish(
            name = "Margherita Pizza",
            description = "Classic Italian pizza with fresh tomato sauce, mozzarella di bufala, and aromatic basil leaves.",
            image = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=800&q=80",
        )

    fun createPizzaIngredientsForDish(dish: Dish) =
        TagContainer(
            tags =
                mutableListOf(
                    Tag("Tomato Sauce"),
                    Tag("Mozzarella"),
                    Tag("Basil"),
                    Tag("Olive Oil"),
                ),
            type = TagContainerType.INGREDIENTS,
            dish = dish,
        )

    fun createPizzaIngredients() =
        TagContainer(
            tags =
                mutableListOf(
                    Tag("Tomato Sauce"),
                    Tag("Mozzarella"),
                    Tag("Basil"),
                    Tag("Olive Oil"),
                ),
            type = TagContainerType.INGREDIENTS,
            dish = null,
        )

    fun createPersonalMargherita(): DishOption {
        val dish = createMargheritaPizza()
        return DishOption(
            dish = dish,
            name = "Personal Margherita (8\")",
            description = "Perfect size for one person with light appetite",
            price = 12.99,
            image = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600&q=80",
            available = true,
            prepTimeMinutes = 15,
            rating = 4.7,
        )
    }

    fun createMediumMargherita(): DishOption {
        val dish = createMargheritaPizza()
        return DishOption(
            dish = dish,
            name = "Medium Margherita (12\")",
            description = "Great for sharing between 2-3 people",
            price = 18.99,
            image = "https://images.unsplash.com/photo-1604382355076-af4b0eb60143?w=600&q=80",
            available = true,
            prepTimeMinutes = 18,
            rating = 4.8,
        )
    }

    fun createLargeMargherita(): DishOption {
        val dish = createMargheritaPizza()
        return DishOption(
            dish = dish,
            name = "Large Margherita (16\")",
            description = "Family size pizza perfect for 4-5 people",
            price = 24.99,
            image = "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=600&q=80",
            available = true,
            prepTimeMinutes = 22,
            rating = 4.9,
        )
    }

    fun createHappyBeansCafe():Restaurant  {
        val user = User(
            email = "hello@world.com",
            firstName = "John",
            lastName = "Doe",
            password = "password",
        )
        return Restaurant(
            user = user,
            name = "Happy Beans Cafe",
            description = "A cozy neighborhood cafe serving fresh, locally-sourced meals and the best coffee in town.",
            image = "https://images.unsplash.com/photo-1554118811-1e0d58224f24?w=800&q=80",
            addressUrl = "https://maps.google.com/?q=Happy+Beans+Cafe+Seoul",
            workingDateHours = (createWeekdaySchedule() + createWeekendSchedule()).toMutableList(),
        )
    }


    fun createMammaMiaPizzeria():Restaurant {
        val user = User(
            email = "hello@world.com",
            firstName = "John",
            lastName = "Doe",
            password = "password",
        )

        return Restaurant(
            user = user,
            name = "Mamma Mia Pizzeria",
            description = "Authentic Italian pizzeria with wood-fired ovens and traditional recipes passed down through generations.",
            image = "https://images.unsplash.com/photo-1565299624946-b28f40a0ca4b?w=800&q=80",
            addressUrl = "https://maps.google.com/?q=Mamma+Mia+Pizzeria+Gangnam",
            workingDateHours =
                createRestaurantHours(
                    LocalTime.of(11, 0),
                    LocalTime.of(24, 0),
                    *DayOfWeek.values(),
                ).toMutableList(),
        )
    }

    fun createMondayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.MONDAY,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(22, 0),
        )

    fun createTuesdayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.TUESDAY,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(22, 0),
        )

    fun createWednesdayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.WEDNESDAY,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(22, 0),
        )

    fun createThursdayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.THURSDAY,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(22, 0),
        )

    fun createFridayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.FRIDAY,
            openTime = LocalTime.of(9, 0),
            closeTime = LocalTime.of(22, 0),
        )

    fun createSaturdayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.SATURDAY,
            openTime = LocalTime.of(10, 0),
            closeTime = LocalTime.of(23, 0),
        )

    fun createSundayHours() =
        WorkingDateHour(
            dayOfWeek = DayOfWeek.SUNDAY,
            openTime = LocalTime.of(10, 0),
            closeTime = LocalTime.of(23, 0),
        )

    fun createWeekdaySchedule() =
        listOf(
            createMondayHours(),
            createTuesdayHours(),
            createWednesdayHours(),
            createThursdayHours(),
            createFridayHours(),
        )

    fun createWeekendSchedule() = listOf(createSaturdayHours(), createSundayHours())

    fun createRestaurantHours(
        openTime: LocalTime,
        closeTime: LocalTime,
        vararg days: DayOfWeek,
    ): List<WorkingDateHour> {
        return days.map { day ->
            WorkingDateHour(day, openTime, closeTime)
        }
    }

    fun createClassicBeefBurger() =
        Dish(
            name = "Classic Beef Burger",
            description = "Juicy beef patty with lettuce, tomato, onion, and our signature sauce on a brioche bun.",
            image = "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=800&q=80",
        )

    fun createSalmonSushiSet() =
        Dish(
            name = "Salmon Sushi Set",
            description = "Fresh Atlantic salmon sushi with avocado, cucumber, and premium sushi rice.",
            image = "https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=800&q=80",
        )

    fun createCaesarSalad() =
        Dish(
            name = "Caesar Salad",
            description = "Crisp romaine lettuce with parmesan cheese, croutons, and our homemade Caesar dressing.",
            image = "https://images.unsplash.com/photo-1546793665-c74683f339c1?w=800&q=80",
        )
}
