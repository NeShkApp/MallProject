package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.ShopItem

interface CartRepository {
    suspend fun addItemToCart(shopItemId: String)
    suspend fun removeItemFromCart(shopItemId: String)
    suspend fun isItemInCart(shopItemId: String): Boolean
    suspend fun getCartItems(): List<ShopItem>
}
