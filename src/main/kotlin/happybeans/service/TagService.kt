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
    fun createTag(tagName: String): Tag {
        return tagRepository.save(Tag(tagName))
    }

    fun findByName(tagName: String): Tag? {
        return tagRepository.findByName(tagName)
    }

    @Transactional
    fun findOrCreateByName(tagName: String): Tag {
        return findByName(tagName) ?: createTag(tagName)
    }
}
