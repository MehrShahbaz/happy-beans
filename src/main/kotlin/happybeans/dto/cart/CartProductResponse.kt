package happybeans.dto.cart

import happybeans.model.CartProduct

class CartProductResponse(cartProduct: CartProduct) {
    val dishName = cartProduct.dish.name
    val dishOptionName = cartProduct.dishOption.name
    val dishPrice = cartProduct.dishOption.price
}
