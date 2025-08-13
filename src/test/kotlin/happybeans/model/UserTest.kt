package happybeans.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class UserTest {
    @Test
    fun fullName() {
        val user = User(
            "test@test.com",
            "12345678",
            "Hello",
            "World"
        )
        assertThat(user.fullName()).isEqualTo("Hello World")
    }
}