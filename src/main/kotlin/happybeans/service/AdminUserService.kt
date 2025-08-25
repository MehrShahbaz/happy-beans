package happybeans.service

import happybeans.dto.user.UserCreateRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.DuplicateEntityException
import happybeans.utils.mapper.toEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminUserService(
    private val userRepository: UserRepository,
) {
    fun createAdmin(request: UserCreateRequestDto): User {
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateEntityException("User already exists")
        }
        val admin = request.toEntity()
        admin.role = UserRole.ADMIN
        return userRepository.save(admin)
    }
}
