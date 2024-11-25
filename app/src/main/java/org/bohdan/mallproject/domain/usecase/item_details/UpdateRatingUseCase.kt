package org.bohdan.mallproject.domain.usecase.item_details

import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class UpdateRatingUseCase @Inject constructor(
    private val shopItemDetailsRepository: ShopItemDetailsRepository
) {
    suspend operator fun invoke(shopItemId: String, newRating: Float){
        shopItemDetailsRepository.updateShopItemRating(shopItemId, newRating)
    }
}