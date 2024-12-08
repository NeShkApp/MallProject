package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.ShopItem

interface FavoriteRepository {
    suspend fun addItemToFavorite(shopItemId: String)
    suspend fun removeItemFromFavorite(shopItemId: String)
    suspend fun isItemInFavorite(shopItemId: String): Boolean
    suspend fun getFavoriteItems(): List<ShopItem>
}