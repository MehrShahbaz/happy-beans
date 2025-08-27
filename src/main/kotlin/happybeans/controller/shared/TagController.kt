package happybeans.controller.shared

import happybeans.dto.response.MessageResponse
import happybeans.dto.tag.TagCreateRequest
import happybeans.model.Tag
import happybeans.service.TagService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/api/tags")
class TagController(private val tagService: TagService) {
    @GetMapping
    fun getAllTags(): ResponseEntity<Map<String, List<Tag>>> {
        return ResponseEntity.ok(mapOf("tags" to tagService.getAllTags()))
    }

    @PostMapping
    fun createTag(
        @Valid @RequestBody tagCreateRequest: TagCreateRequest,
    ): ResponseEntity<MessageResponse> {
        val tag = tagService.createTag(tagCreateRequest.name)
        val location =
            ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("create-tag")
                .buildAndExpand(tag.id)
                .toUri()

        return ResponseEntity.created(location).body(MessageResponse("Tag created!"))
    }
}
