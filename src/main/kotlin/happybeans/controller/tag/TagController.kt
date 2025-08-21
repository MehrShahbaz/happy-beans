package happybeans.controller.tag

import happybeans.service.TagService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/tag")
class TagController(
    private val tagService: TagService,
) {
    // TODO: Implement tag-related endpoints
    // This controller will handle tag operations for ingredients, likes, and dislikes
    // Example endpoints:
    // GET /api/tag/dish/{dishId}/ingredients - Get ingredients for a dish
    // POST /api/tag/dish/{dishId}/ingredients - Add ingredients to a dish
    // GET /api/tag/user/{userId}/likes - Get user's liked tags
    // POST /api/tag/user/{userId}/likes - Add tags to user's likes

//    @PostMapping createTag(
//        @Valid @RequestBody tagCreateRequest
//    ): {
//        tagService.findOrCreate
//    }
}
