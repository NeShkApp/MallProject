package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.ShopItem

interface ShopItemDetailsRepository {
    suspend fun getShopItemDetailsById(shopItemId: String): ShopItem
    suspend fun updateShopItemRating(shopItemId: String, newRating: Float)


}