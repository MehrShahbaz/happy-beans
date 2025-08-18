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
    fun findOrCreateTags(ingredientNames: Set<String>): Set<Tag> {
        return ingredientNames.map { name ->
            tagRepository.findByNameOrNull(name)
                ?: Tag(name = name)
        }.toSet()
    }
}
