package happybeans.utils.mapper

import happybeans.dto.user.UserCreateRequestDto
import happybeans.model.User

fun UserCreateRequestDto.toEntity(): User {
    return User(
        email,
        password,
        firstName,
        lastName,
    )
}
