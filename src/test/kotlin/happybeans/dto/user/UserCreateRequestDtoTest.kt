package happybeans.dto.user

import jakarta.validation.Validation
import jakarta.validation.Validator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class UserCreateRequestDtoTest {
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
            UserCreateRequestDto(
                email = "test@example.com",
                password = "123456",
                firstName = "John",
                lastName = "Doe",
            )

        val violations = validator.validate(dto)

        assertThat(violations).isEmpty()
    }

    @Test
    fun `blank email should fail with NotBlank message`() {
        assertThrows<IllegalArgumentException> {
            UserCreateRequestDto(
                email = " ",
                password = "123456",
                firstName = "John",
                lastName = "Doe",
            )
        }
    }

    @Test
    fun `invalid email format should fail with Email message`() {
        val dto =
            UserCreateRequestDto(
                email = "invalid-email",
                password = "123456",
                firstName = "John",
                lastName = "Doe",
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
            UserCreateRequestDto(
                email = "test@example.com",
                password = "123",
                firstName = "John",
                lastName = "Doe",
            )

        val violations = validator.validate(dto)

        assertThat(violations).anySatisfy { v ->
            assertThat(v.propertyPath.toString()).isEqualTo("password")
            assertThat(v.message).isEqualTo("Password must be at least 6 characters")
        }
    }

    @Test
    fun `short firstName should fail length validation`() {
        val dto =
            UserCreateRequestDto(
                email = "test@example.com",
                password = "123456",
                firstName = "Al",
                lastName = "Doe",
            )

        val violations = validator.validate(dto)

        assertThat(violations).anySatisfy { v ->
            assertThat(v.propertyPath.toString()).isEqualTo("firstName")
            assertThat(v.message).isEqualTo("FirstName must be at least 3 characters")
        }
    }
}
