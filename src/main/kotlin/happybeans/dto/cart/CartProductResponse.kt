package happybeans.dto.cart

import happybeans.model.CartProduct

class CartProductResponse(
    val dishName: String,
    val dishOptionName: String,
    val dishPrice: Double,
) {
    constructor(cartProduct: CartProduct) : this(
        cartProduct.dish.name,
        cartProduct.dishOption.name,
        cartProduct.dishOption.price,
    )
}
