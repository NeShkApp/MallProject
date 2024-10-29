package org.bohdan.mallproject.domain.usecase.home

import androidx.lifecycle.LiveData
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
    ): LiveData<List<ShopItem>> {
        return homeRepository.getShopItemByAttributes(
            sortBy = sortBy,
            name = name,
            hasDiscount = hasDiscount,
            availableInStock = availableInStock,
            category = category,
            minPrice = minPrice,
            maxPrice = maxPrice,
            minRating = minRating
        )
    }
}