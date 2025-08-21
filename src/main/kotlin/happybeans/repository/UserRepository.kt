package happybeans.repository

import happybeans.enums.UserRole
import happybeans.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): Optional<User>

    fun findByEmailAndPassword(
        email: String,
        password: String,
    ): Optional<User>

    fun existsByEmail(email: String): Boolean

    fun existsByEmailAndRole(email: String, role: UserRole):Boolean
}
