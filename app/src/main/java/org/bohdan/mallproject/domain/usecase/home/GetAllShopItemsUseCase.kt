package org.bohdan.mallproject.domain.usecase.home

import androidx.lifecycle.LiveData
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.HomeRepository

class GetAllShopItemsUseCase(
    private val homeRepository: HomeRepository
) {
    operator fun invoke(): LiveData<List<ShopItem>>{
        return homeRepository.getAllShopItems()
    }
}