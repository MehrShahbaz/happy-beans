package happybeans.repository

import happybeans.model.DishOption
import org.springframework.data.jpa.repository.JpaRepository

interface DishOptionRepository : JpaRepository<DishOption, Long>
