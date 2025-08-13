package happybeans.dto.user

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

class UserCreateRequestDto(
    @field:NotBlank(message = "Email cannot be blank")
    @field:Email(message = "Must be a valid email address")
    var email: String,
    @field:Length(min = 6, message = "Password must be at least 6 characters")
    var password: String,
    @field:Length(min = 3, message = "FirstName must be at least 3 characters")
    var firstName: String,
    var lastName: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
    }
}
