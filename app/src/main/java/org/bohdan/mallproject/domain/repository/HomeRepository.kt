package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory

interface HomeRepository {
    suspend fun getAllShopItems(): List<ShopItem>
    suspend fun getAllCategories(): List<Category>
    suspend fun getSubcategoriesByCategory(category: Category): List<Subcategory>
    suspend fun getShopItemsByFilters(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?
    ): List<ShopItem>
    fun sortShopItems(items: List<ShopItem>, sortBy: SortBy?) : List<ShopItem>
}