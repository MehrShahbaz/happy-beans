package happybeans.service

import happybeans.enums.UserRole
import happybeans.model.Tag
import happybeans.model.User
import happybeans.repository.UserRepository
import happybeans.utils.exception.EntityNotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.verify
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var tagService: TagService

    @InjectMocks
    private lateinit var userService: UserService

    private val testUser =
        User(
            email = "test@user.com",
            password = "password",
            firstName = "Test",
            lastName = "User",
            role = UserRole.USER,
        ).apply { id = 1L }

    @Test
    fun `findById should return user when user exists`() {
        // Given
        val userId = 1L
        given(userRepository.findById(userId)).willReturn(Optional.of(testUser))

        // When
        val result = userService.findById(userId)

        // Then
        assertThat(result).isEqualTo(testUser)
    }

    @Test
    fun `findById should throw EntityNotFoundException when user not found`() {
        // Given
        val userId = 999L
        given(userRepository.findById(userId)).willReturn(Optional.empty())

        // When & Then
        assertThrows<EntityNotFoundException> {
            userService.findById(userId)
        }
    }

    @Test
    fun `addUserLike should add tag to likes and remove from dislikes`() {
        // Given
        val userId = 1L
        val tagName = "spicy"
        val tag = Tag(name = tagName, id = 1L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                dislikes.add(tag) // User initially dislikes this tag
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findOrCreateByName(tagName)).willReturn(tag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.addUserLike(userId, tagName)

        // Then
        assertThat(result.likes).contains(tag)
        assertThat(result.dislikes).doesNotContain(tag)
        verify(tagService).findOrCreateByName(tagName)
        verify(userRepository).save(user)
    }

    @Test
    fun `removeUserLike should remove tag from likes`() {
        // Given
        val userId = 1L
        val tagName = "spicy"
        val tag = Tag(name = tagName, id = 1L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                likes.add(tag)
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findByName(tagName)).willReturn(tag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.removeUserLike(userId, tagName)

        // Then
        assertThat(result.likes).doesNotContain(tag)
        verify(tagService).findByName(tagName)
        verify(userRepository).save(user)
    }

    @Test
    fun `addUserDislike should add tag to dislikes and remove from likes`() {
        // Given
        val userId = 1L
        val tagName = "meat"
        val tag = Tag(name = tagName, id = 1L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                // User initially likes this tag
                likes.add(tag)
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findOrCreateByName(tagName)).willReturn(tag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.addUserDislike(userId, tagName)

        // Then
        assertThat(result.dislikes).contains(tag)
        assertThat(result.likes).doesNotContain(tag)
        verify(tagService).findOrCreateByName(tagName)
        verify(userRepository).save(user)
    }

    @Test
    fun `removeUserDislike should remove tag from dislikes`() {
        // Given
        val userId = 1L
        val tagName = "meat"
        val tag = Tag(name = tagName, id = 1L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                dislikes.add(tag)
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findByName(tagName)).willReturn(tag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.removeUserDislike(userId, tagName)

        // Then
        assertThat(result.dislikes).doesNotContain(tag)
        verify(tagService).findByName(tagName)
        verify(userRepository).save(user)
    }

    @Test
    fun `updateUserLikes should replace all likes and ensure no conflicts with dislikes`() {
        // Given
        val userId = 1L
        val tagNames = setOf("spicy", "vegetarian")
        val spicyTag = Tag(name = "spicy", id = 1L)
        val vegetarianTag = Tag(name = "vegetarian", id = 2L)
        val oldTag = Tag(name = "old", id = 3L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                likes.add(oldTag)
                dislikes.add(spicyTag) // Spicy was in dislikes, should be removed
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findOrCreateByName("spicy")).willReturn(spicyTag)
        given(tagService.findOrCreateByName("vegetarian")).willReturn(vegetarianTag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.updateUserLikes(userId, tagNames)

        // Then
        assertThat(result.likes).containsExactlyInAnyOrder(spicyTag, vegetarianTag)
        assertThat(result.likes).doesNotContain(oldTag)
        assertThat(result.dislikes).doesNotContain(spicyTag)
        verify(tagService).findOrCreateByName("spicy")
        verify(tagService).findOrCreateByName("vegetarian")
        verify(userRepository).save(user)
    }

    @Test
    fun `updateUserDislikes should replace all dislikes and ensure no conflicts with likes`() {
        // Given
        val userId = 1L
        val tagNames = setOf("meat", "dairy")
        val meatTag = Tag(name = "meat", id = 1L)
        val dairyTag = Tag(name = "dairy", id = 2L)
        val oldTag = Tag(name = "old", id = 3L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                dislikes.add(oldTag)
                // Meat was in likes, should be removed
                likes.add(meatTag)
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))
        given(tagService.findOrCreateByName("meat")).willReturn(meatTag)
        given(tagService.findOrCreateByName("dairy")).willReturn(dairyTag)
        given(userRepository.save(user)).willReturn(user)

        // When
        val result = userService.updateUserDislikes(userId, tagNames)

        // Then
        assertThat(result.dislikes).containsExactlyInAnyOrder(meatTag, dairyTag)
        assertThat(result.dislikes).doesNotContain(oldTag)
        assertThat(result.likes).doesNotContain(meatTag)
        verify(tagService).findOrCreateByName("meat")
        verify(tagService).findOrCreateByName("dairy")
        verify(userRepository).save(user)
    }

    @Test
    fun `getUserLikes should return user's likes`() {
        // Given
        val userId = 1L
        val spicyTag = Tag(name = "spicy", id = 1L)
        val vegetarianTag = Tag(name = "vegetarian", id = 2L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                likes.addAll(setOf(spicyTag, vegetarianTag))
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))

        // When
        val result = userService.getUserLikes(userId)

        // Then
        assertThat(result).containsExactlyInAnyOrder(spicyTag, vegetarianTag)
    }

    @Test
    fun `getUserDislikes should return user's dislikes`() {
        // Given
        val userId = 1L
        val meatTag = Tag(name = "meat", id = 1L)
        val dairyTag = Tag(name = "dairy", id = 2L)
        val user =
            User(
                email = "test@user.com",
                password = "password",
                firstName = "Test",
                lastName = "User",
                role = UserRole.USER,
            ).apply {
                id = userId
                dislikes.addAll(setOf(meatTag, dairyTag))
            }

        given(userRepository.findById(userId)).willReturn(Optional.of(user))

        // When
        val result = userService.getUserDislikes(userId)

        // Then
        assertThat(result).containsExactlyInAnyOrder(meatTag, dairyTag)
    }
}
