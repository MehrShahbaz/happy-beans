package happybeans.dto.cart

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive

class CartProductRequest(
    @field:NotNull
    @field:Positive
    @field:Min(1)
    val quantity: Int = 1,
)
