package org.bohdan.mallproject.domain.usecase.cart

import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class CalculateTotalPriceUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(): Double {
        return cartRepository.calculateTotalPrice()
    }
}