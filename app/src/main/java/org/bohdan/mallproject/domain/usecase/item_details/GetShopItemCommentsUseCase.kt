package org.bohdan.mallproject.domain.usecase.item_details

import org.bohdan.mallproject.domain.model.Comment
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class GetShopItemCommentsUseCase @Inject constructor(
    private val shopItemDetailsRepository: ShopItemDetailsRepository
) {
    suspend operator fun invoke(shopItemId: String): List<Comment>{
        return shopItemDetailsRepository.getShopItemComments(shopItemId)
    }
}