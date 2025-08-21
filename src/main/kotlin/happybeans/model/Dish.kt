package happybeans.model

import happybeans.utils.exception.EntityNotFoundException
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "dishes")
class Dish(
    @Column(name = "name", nullable = false)
    var name: String,
    @Column(name = "description", nullable = false)
    var description: String,
    @Column(name = "image", nullable = false)
    var image: String,
    @OneToMany(mappedBy = "dish", cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    var dishOptions: MutableSet<DishOption> = mutableSetOf(),
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,
) {
    fun addDishOption(option: DishOption) {
        option.dish = this
        dishOptions.add(option)
    }

    fun addDishOptions(dishOptions: List<DishOption>) {
        dishOptions.forEach { option ->
            addDishOption(option)
        }
    }

    fun removeDishOption(option: DishOption) {
        dishOptions.remove(option)
    }

    fun setDishOptionAvailability(
        optionId: Long,
        available: Boolean,
    ) {
        dishOptions.find { it.id == optionId }?.let { option ->
            option.available = available
        } ?: throw EntityNotFoundException("Dish option with id $optionId not found in dish $id")
    }

    fun enableDishOption(optionId: Long) {
        setDishOptionAvailability(optionId, true)
    }

    fun disableDishOption(optionId: Long) {
        setDishOptionAvailability(optionId, false)
    }

    fun getAvailableOptions(): List<DishOption> {
        return dishOptions.filter { it.available }
    }

    fun hasAvailableOptions(): Boolean {
        return dishOptions.any { it.available }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Dish) return false
        return id != 0L && id == other.id
    }

    override fun hashCode(): Int = java.util.Objects.hash(id)

    override fun toString(): String = "Dish(id=$id, name='$name')"
}
