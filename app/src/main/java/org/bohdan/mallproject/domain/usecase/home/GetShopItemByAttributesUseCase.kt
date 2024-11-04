package org.bohdan.mallproject.domain.usecase.home

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.repository.HomeRepository

class GetShopItemByAttributesUseCase(
    private val homeRepository: HomeRepository
) {

    suspend operator fun invoke(
        sortBy: SortBy,
        name: String? = null,
        hasDiscount: Boolean? = null,
        availableInStock: Boolean = false,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minRating: Float? = null,
    ): List<ShopItem> {
        return homeRepository.getShopItemByAttributes(
            sortBy,
            name,
            hasDiscount,
            availableInStock,
            category,
            minPrice,
            maxPrice,
            minRating
        )
    }
}