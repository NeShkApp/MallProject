package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class CheckIfItemInCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(shopItemId: String): Boolean {
        return cartRepository.isItemInCart(shopItemId)
    }
}
