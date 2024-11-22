package org.bohdan.mallproject.domain.usecase.item_details

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository

class GetShopItemDetailsByIdUseCase(
    private val shopItemDetailsRepository: ShopItemDetailsRepository
) {
    suspend operator fun invoke(shopItemId: String): ShopItem {
        return shopItemDetailsRepository.getShopItemDetailsById(shopItemId)
    }
}