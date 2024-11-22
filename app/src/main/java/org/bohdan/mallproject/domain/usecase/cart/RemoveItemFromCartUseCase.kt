package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.CartRepository

class RemoveItemFromCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(shopItemId: String){
        return cartRepository.removeItemFromCart(shopItemId)
    }
}