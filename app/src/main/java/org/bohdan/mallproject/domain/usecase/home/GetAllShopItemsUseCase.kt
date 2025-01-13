package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.HomeRepository
import javax.inject.Inject

class GetAllShopItemsUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): List<ShopItem>{
        return homeRepository.getAllShopItems()
    }
}