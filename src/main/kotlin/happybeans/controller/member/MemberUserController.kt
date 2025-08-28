package happybeans.controller.member

import happybeans.dto.response.MessageResponse
import happybeans.dto.tag.TagRequest
import happybeans.model.Tag
import happybeans.model.User
import happybeans.service.UserService
import happybeans.utils.annotations.LoginMember
import jakarta.validation.Valid
import mu.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/member")
class MemberUserController(
    private val userService: UserService,
) {
    private val logger = KotlinLogging.logger {}

    @GetMapping("/likes")
    @Transactional(readOnly = true)
    fun getUserLikes(
        @LoginMember user: User,
    ): ResponseEntity<Set<Tag>> {
        logger.info { "Getting likes for ${user.id}" }
        val likes = userService.getUserLikes(user.id)
        return ResponseEntity.ok(likes)
    }

    @PostMapping("/likes")
    fun addUserLike(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Adding like for ${user.id}" }
        val tagNames = tagRequest.tagNames

        tagNames.forEach { tagName ->
            userService.addUserLike(user.id, tagName)
        }

        return if (tagNames.size == 1) {
            ResponseEntity.ok(MessageResponse("Tag added to likes successfully"))
        } else {
            ResponseEntity.ok(MessageResponse("Tags added to likes successfully"))
        }
    }

    @DeleteMapping("/likes")
    fun removeUserLike(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Removing like for ${user.id}" }
        val tagNames = tagRequest.tagNames

        tagNames.forEach { tagName ->
            userService.removeUserLike(user.id, tagName)
        }

        return if (tagNames.size == 1) {
            ResponseEntity.ok(MessageResponse("Tag removed from likes successfully"))
        } else {
            ResponseEntity.ok(MessageResponse("Tags removed from likes successfully"))
        }
    }

    @PutMapping("/likes")
    fun updateUserLikes(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Updating likes for ${user.id}" }
        val tagNames = tagRequest.tagNames.toSet()
        userService.updateUserLikes(user.id, tagNames)
        return ResponseEntity.ok(MessageResponse("Likes updated successfully"))
    }

    @GetMapping("/dislikes")
    @Transactional(readOnly = true)
    fun getUserDislikes(
        @LoginMember user: User,
    ): ResponseEntity<Set<Tag>> {
        logger.info { "Getting dislikes for ${user.id}" }
        val dislikes = userService.getUserDislikes(user.id)
        return ResponseEntity.ok(dislikes)
    }

    @PostMapping("/dislikes")
    fun addUserDislike(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Adding dislike for ${user.id}" }
        val tagNames = tagRequest.tagNames

        tagNames.forEach { tagName ->
            userService.addUserDislike(user.id, tagName)
        }

        return if (tagNames.size == 1) {
            ResponseEntity.ok(MessageResponse("Tag added to dislikes successfully"))
        } else {
            ResponseEntity.ok(MessageResponse("Tags added to dislikes successfully"))
        }
    }

    @DeleteMapping("/dislikes")
    fun removeUserDislike(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Removing dislike for ${user.id}" }
        val tagNames = tagRequest.tagNames

        tagNames.forEach { tagName ->
            userService.removeUserDislike(user.id, tagName)
        }

        return if (tagNames.size == 1) {
            ResponseEntity.ok(MessageResponse("Tag removed from dislikes successfully"))
        } else {
            ResponseEntity.ok(MessageResponse("Tags removed from dislikes successfully"))
        }
    }

    @PutMapping("/dislikes")
    fun updateUserDislikes(
        @LoginMember user: User,
        @Valid @RequestBody tagRequest: TagRequest,
    ): ResponseEntity<MessageResponse> {
        logger.info { "Updating dislikes for ${user.id}" }
        val tagNames = tagRequest.tagNames.toSet()
        userService.updateUserDislikes(user.id, tagNames)
        return ResponseEntity.ok(MessageResponse("Dislikes updated successfully"))
    }
}
