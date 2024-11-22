package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.repository.CartRepository

class CheckIfItemInCartUseCase(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(shopItemId: String): Boolean {
        return cartRepository.isItemInCart(shopItemId)
    }
}
