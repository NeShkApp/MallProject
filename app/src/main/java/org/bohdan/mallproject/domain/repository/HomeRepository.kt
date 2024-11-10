package org.bohdan.mallproject.domain.repository

import androidx.lifecycle.LiveData
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.usecase.home.GetAllShopItemsUseCase

interface HomeRepository {
    suspend fun getAllShopItems(): List<ShopItem>
    suspend fun getShopItemById(shopItemId: String): ShopItem
    suspend fun getAllCategories(): List<Category>
    suspend fun getShopItemByAttributes(
        sortBy: SortBy,
        name: String? = null,
        hasDiscount: Boolean? = null,
        availableInStock: Boolean = false,
        category: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null,
        minRating: Float? = null,
    ): List<ShopItem>
//    suspend fun getSubcategoriesNamesByCategoryId(categoryId: String): List<String>
    suspend fun getSubcategoriesByCategory(category: Category): List<Subcategory>
}