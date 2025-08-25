package happybeans.service

import happybeans.model.Tag
import happybeans.repository.TagRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagService(
    private val tagRepository: TagRepository,
) {
    fun createTag(tagName: String): Tag {
        return tagRepository.save(Tag(tagName))
    }

    @Transactional(readOnly = true)
    fun findByName(tagName: String): Tag? {
        return tagRepository.findByName(tagName)
    }

    fun findOrCreateByName(tagName: String): Tag {
        return findByName(tagName) ?: createTag(tagName)
    }
}
