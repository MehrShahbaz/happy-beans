package happybeans.service

import happybeans.model.Tag
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val tagService: TagService,
) {
    fun findById(userId: Long): User {
        return userRepository.findById(userId)
            .orElseThrow { EntityNotFoundException("User with id '$userId' not found") }
    }

    fun addUserLike(
        userId: Long,
        tagName: String,
    ): User {
        val user = findById(userId)
        val tag = tagService.findOrCreateByName(tagName)

        // Add and remove from dislikes if it exists
        user.likes.add(tag)
        user.dislikes.remove(tag)

        return userRepository.save(user)
    }

    fun removeUserLike(
        userId: Long,
        tagName: String,
    ): User {
        val user = findById(userId)
        val tag = tagService.findByName(tagName)

        if (tag != null) {
            user.likes.remove(tag)
        }

        return userRepository.save(user)
    }

    fun addUserDislike(
        userId: Long,
        tagName: String,
    ): User {
        val user = findById(userId)
        val tag = tagService.findOrCreateByName(tagName)

        // Add and remove from likes if it exists
        user.dislikes.add(tag)
        user.likes.remove(tag)

        return userRepository.save(user)
    }

    fun removeUserDislike(
        userId: Long,
        tagName: String,
    ): User {
        val user = findById(userId)
        val tag = tagService.findByName(tagName)

        if (tag != null) {
            user.dislikes.remove(tag)
        }

        return userRepository.save(user)
    }

    fun updateUserLikes(
        userId: Long,
        tagNames: Set<String>,
    ): User {
        val user = findById(userId)

        user.likes.clear()
        tagNames.forEach { tagName ->
            val tag = tagService.findOrCreateByName(tagName)
            // Update and ensure no conflicts
            user.likes.add(tag)
            user.dislikes.remove(tag)
        }

        return userRepository.save(user)
    }

    fun updateUserDislikes(
        userId: Long,
        tagNames: Set<String>,
    ): User {
        val user = findById(userId)

        user.dislikes.clear()
        tagNames.forEach { tagName ->
            val tag = tagService.findOrCreateByName(tagName)
            // Update and ensure no conflicts
            user.dislikes.add(tag)
            user.likes.remove(tag)
        }

        return userRepository.save(user)
    }

    fun getUserLikes(userId: Long): Set<Tag> {
        val user = findById(userId)
        return user.likes
    }

    fun getUserDislikes(userId: Long): Set<Tag> {
        val user = findById(userId)
        return user.dislikes
    }
}
