package happybeans.service

import happybeans.dto.user.UserCreateRequestDto
import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.DuplicateEntityException
import happybeans.utils.mapper.toEntity
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AdminUserService(
    private val userRepository: UserRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun createAdmin(request: UserCreateRequestDto): User {
        if (userRepository.existsByEmail(request.email)) {
            logger.error("Admin already exists with email ${request.email}")
            throw DuplicateEntityException("User already exists")
        }
        val admin = request.toEntity()
        admin.role = UserRole.ADMIN
        logger.info { "Admin created successfully with role ${admin.email}" }
        return userRepository.save(admin)
    }
}
