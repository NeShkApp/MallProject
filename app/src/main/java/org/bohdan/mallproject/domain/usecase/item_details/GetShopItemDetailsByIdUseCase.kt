package org.bohdan.mallproject.domain.usecase.item_details

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class GetShopItemDetailsByIdUseCase @Inject constructor(
    private val shopItemDetailsRepository: ShopItemDetailsRepository
) {
    suspend operator fun invoke(shopItemId: String): ShopItem {
        return shopItemDetailsRepository.getShopItemDetailsById(shopItemId)
    }
}