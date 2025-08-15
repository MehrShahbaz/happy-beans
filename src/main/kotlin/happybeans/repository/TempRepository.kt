package happybeans.repository

import happybeans.model.Temp
import org.springframework.data.jpa.repository.JpaRepository

interface TempRepository: JpaRepository<Temp, Long> {
    fun findByUserIdAndType()
}