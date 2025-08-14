package happybeans.utils.exception

import java.lang.RuntimeException

class UserAlreadyExistsException(email: String) : RuntimeException("User already exists with email $email")
