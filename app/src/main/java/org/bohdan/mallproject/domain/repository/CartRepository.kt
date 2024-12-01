package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.ShopItem

interface CartRepository {
    suspend fun addItemToCart(shopItemId: String)
    suspend fun removeItemFromCart(shopItemId: String)
    suspend fun isItemInCart(shopItemId: String): Boolean
    suspend fun getCartItems(): List<ShopItem>
    suspend fun updateSelectedQuantity(shopItemId: String, quantity: Int)
    suspend fun calculateTotalPrice(): Double
    suspend fun updateProductQuantityInStock(shopItemId: String, newQuantity: Int)
    suspend fun clearCart()
}
