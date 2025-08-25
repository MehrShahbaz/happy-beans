package happybeans.controller.member

import happybeans.dto.response.MessageResponse
import happybeans.model.Tag
import happybeans.model.User
import happybeans.service.UserService
import happybeans.utils.annotations.LoginMember
import org.springframework.http.ResponseEntity
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
    @GetMapping("/likes")
    fun getUserLikes(
        @LoginMember user: User,
    ): ResponseEntity<Set<Tag>> {
        val likes = userService.getUserLikes(user.id)
        return ResponseEntity.ok(likes)
    }

    @PostMapping("/likes")
    fun addUserLike(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        userService.addUserLike(user.id, tagName)
        return ResponseEntity.ok(MessageResponse("Tag added to likes successfully"))
    }

    @DeleteMapping("/likes")
    fun removeUserLike(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        userService.removeUserLike(user.id, tagName)
        return ResponseEntity.ok(MessageResponse("Tag removed from likes successfully"))
    }

    @PutMapping("/likes")
    fun updateUserLikes(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, Set<String>>,
    ): ResponseEntity<MessageResponse> {
        val tagNames = tagRequest["tagNames"] ?: throw IllegalArgumentException("tagNames is required")
        userService.updateUserLikes(user.id, tagNames)
        return ResponseEntity.ok(MessageResponse("Likes updated successfully"))
    }

    @GetMapping("/dislikes")
    fun getUserDislikes(
        @LoginMember user: User,
    ): ResponseEntity<Set<Tag>> {
        val dislikes = userService.getUserDislikes(user.id)
        return ResponseEntity.ok(dislikes)
    }

    @PostMapping("/dislikes")
    fun addUserDislike(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        userService.addUserDislike(user.id, tagName)
        return ResponseEntity.ok(MessageResponse("Tag added to dislikes successfully"))
    }

    @DeleteMapping("/dislikes")
    fun removeUserDislike(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, String>,
    ): ResponseEntity<MessageResponse> {
        val tagName = tagRequest["tagName"] ?: throw IllegalArgumentException("tagName is required")
        userService.removeUserDislike(user.id, tagName)
        return ResponseEntity.ok(MessageResponse("Tag removed from dislikes successfully"))
    }

    @PutMapping("/dislikes")
    fun updateUserDislikes(
        @LoginMember user: User,
        @RequestBody tagRequest: Map<String, Set<String>>,
    ): ResponseEntity<MessageResponse> {
        val tagNames = tagRequest["tagNames"] ?: throw IllegalArgumentException("tagNames is required")
        userService.updateUserDislikes(user.id, tagNames)
        return ResponseEntity.ok(MessageResponse("Dislikes updated successfully"))
    }
}
