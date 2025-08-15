package happybeans.config

import happybeans.enums.UserRole
import happybeans.model.User
import happybeans.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer() : CommandLineRunner {
    @Autowired
    lateinit var userRepository: UserRepository

    override fun run(vararg args: String?) {
        if (userRepository.count() == 0L) {
            userRepository.save(
                User(
                    "admin@admin.com",
                    "12345678",
                    "admin",
                    "Admin",
                    UserRole.ADMIN,
                ),
            )
        }
    }
}
