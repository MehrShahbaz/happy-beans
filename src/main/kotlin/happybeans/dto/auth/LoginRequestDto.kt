package happybeans.dto.auth

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

class LoginRequestDto(
    @field:Email(message = "Must be a valid email address")
    val email: String,
    @field:Length(min = 6, message = "Password must be at least 6 characters")
    var password: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
    }
}
