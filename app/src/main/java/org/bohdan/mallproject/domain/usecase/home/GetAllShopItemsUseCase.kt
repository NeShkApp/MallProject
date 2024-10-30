package org.bohdan.mallproject.domain.usecase.home

import androidx.lifecycle.LiveData
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.HomeRepository

class GetAllShopItemsUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): List<ShopItem>{
        return homeRepository.getAllShopItems()
    }
}