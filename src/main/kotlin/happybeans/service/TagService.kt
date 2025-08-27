package happybeans.service

import happybeans.model.Tag
import happybeans.repository.TagRepository
import happybeans.utils.exception.DuplicateEntityException
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagService(
    private val tagRepository: TagRepository,
) {
    private val logger = KotlinLogging.logger {}

    fun createTag(tagName: String): Tag {
        logger.info { "Creating tag: $tagName" }
        if (tagRepository.existsByName(tagName)) {
            logger.warn { "Tag already exists: $tagName" }
            throw DuplicateEntityException("Tag already exists!")
        }
        return tagRepository.save(Tag(tagName))
    }

    fun getAllTags(): List<Tag> {
        logger.info { "Getting all tags" }
        return tagRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun findByName(tagName: String): Tag? {
        return tagRepository.findByName(tagName)
    }

    fun findOrCreateByName(tagName: String): Tag {
        return findByName(tagName) ?: createTag(tagName)
    }
}
