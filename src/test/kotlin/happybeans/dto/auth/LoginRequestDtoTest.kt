package happybeans.dto.auth

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class LoginRequestDtoTest {
    companion object {
        private lateinit var validator: Validator

        @JvmStatic
        @BeforeAll
        fun setUpValidator() {
            validator = Validation.buildDefaultValidatorFactory().validator
        }
    }

    @Test
    fun `valid DTO should have no violations`() {
        val dto =
            LoginRequestDto(
                email = "test@example.com",
                password = "123456",
            )

        val violations = validator.validate(dto)

        assertThat(violations).isEmpty()
    }

    @Test
    fun `blank email should fail with NotBlank message`() {
        assertThrows<IllegalArgumentException> {
            LoginRequestDto(
                email = " ",
                password = "123456",
            )
        }
    }

    @Test
    fun `invalid email format should fail with Email message`() {
        val dto =
            LoginRequestDto(
                email = "invalid-email",
                password = "123456",
            )

        val violations = validator.validate(dto)

        assertThat(violations).anySatisfy { v ->
            assertThat(v.propertyPath.toString()).isEqualTo("email")
            assertThat(v.message).isEqualTo("Must be a valid email address")
        }
    }

    @Test
    fun `short password should fail length validation`() {
        val dto =
            LoginRequestDto(
                email = "test@example.com",
                password = "123",
            )

        val violations = validator.validate(dto)

        assertThat(violations).anySatisfy { v ->
            assertThat(v.propertyPath.toString()).isEqualTo("password")
            assertThat(v.message).isEqualTo("Password must be at least 6 characters")
        }
    }
}
