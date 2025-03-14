package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class RemoveItemFromCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(shopItemId: String){
        return cartRepository.removeItemFromCart(shopItemId)
    }
}