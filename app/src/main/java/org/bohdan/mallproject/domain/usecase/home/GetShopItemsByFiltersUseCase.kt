package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.repository.HomeRepository

class GetShopItemsByFiltersUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?
    ): List<ShopItem>
    {
        return homeRepository.getShopItemsByFilters(category, subcategory, searchQuery)
    }
}