package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.Comment
import org.bohdan.mallproject.domain.model.ShopItem

interface ShopItemDetailsRepository {
    suspend fun getShopItemDetailsById(shopItemId: String): ShopItem
    suspend fun updateShopItemRating(shopItemId: String)
    suspend fun getShopItemComments(shopItemId: String): List<Comment>

    suspend fun hasUserPurchasedProduct(shopItemId: String): Boolean
    suspend fun hasUserAlreadyReviewed(shopItemId: String): Boolean
    suspend fun canUserLeaveComment(shopItemId: String): Boolean
    suspend fun submitReview(shopItemId: String, rating: Float, text: String)
}