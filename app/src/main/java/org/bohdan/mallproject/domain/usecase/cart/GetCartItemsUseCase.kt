package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class GetCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): List<ShopItem>{
        return cartRepository.getCartItems()
    }
}