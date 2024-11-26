package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.repository.HomeRepository
import javax.inject.Inject

class SortShopItemsByFiltersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
){
    operator fun invoke(items: List<ShopItem>, sortBy: SortBy?) : List<ShopItem>{
        return homeRepository.sortShopItems(items, sortBy)
    }
}