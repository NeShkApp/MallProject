package org.bohdan.mallproject.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class ShopItemDetailsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
): ShopItemDetailsRepository {
    override suspend fun getShopItemDetailsById(shopItemId: String): ShopItem {
        return try {
            val document = firestore.collection("products").document(shopItemId).get().await()
            val shopItem = document.toObject(ShopItem::class.java)?.copy(id = document.id)
                ?: throw NoSuchElementException("Shop item with ID $shopItemId not found.")
            Log.d("ShopItemDetailsRepositoryImpl", "getShopItemDetailsById: ${shopItem.toString()}")
            shopItem
        } catch (e: Exception) {
            Log.e("ShopItemDetailsRepositoryImpl", "Error getting document by ID", e)
            throw e
        }
    }

    override suspend fun updateShopItemRating(shopItemId: String, newRating: Float) {
        TODO("Not yet implemented")
    }
}