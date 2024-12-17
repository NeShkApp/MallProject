package org.bohdan.mallproject.domain.usecase.item_details

import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class SubmitReviewUseCase @Inject constructor(
    private val shopItemDetailsRepository: ShopItemDetailsRepository
) {
    suspend operator fun invoke(shopItemId: String, rating: Float, text: String){
        return shopItemDetailsRepository.submitReview(shopItemId, rating, text)
    }
}