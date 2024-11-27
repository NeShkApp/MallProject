package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class UpdateSelectedQuantityUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(shopItemId: String, quantity: Int){
        cartRepository.updateSelectedQuantity(shopItemId, quantity)
    }
}