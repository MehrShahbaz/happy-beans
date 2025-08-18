package happybeans.utils.exception

class UserCredentialException(message: String? = "Either password or email incorrect") : RuntimeException(message)
