package happybeans.service

import happybeans.model.Tag
import happybeans.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class TagService(
    private val tagRepository: TagRepository,
) {
    @Transactional
    fun createTag(tag: String): Tag {
        return tagRepository.save(Tag(tag))
    }

    fun getDishIngredients() {
    }

    fun getUserLikes() {
    }

    fun getUserDislikes() {
    }

    fun deleteTag() {
    }
}
