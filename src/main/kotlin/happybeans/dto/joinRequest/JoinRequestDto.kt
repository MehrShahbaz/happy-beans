package happybeans.dto.joinRequest

import jakarta.validation.constraints.Email
import org.hibernate.validator.constraints.Length

class JoinRequestDto(
    @field:Email(message = "Must be a valid email address")
    var email: String,
    @field:Length(min = 3, message = "First Name must be at least 3 characters")
    var firstName: String,
    @field:Length(min = 3, message = "Last Name must be at least 3 characters")
    var lastName: String,
    @field:Length(min = 3, max = 1000, message = "Message must be at max 1000 characters")
    var message: String,
) {
    init {
        require(email.isNotBlank()) { "Email cannot be blank" }
    }
}
