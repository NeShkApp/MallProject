package org.bohdan.mallproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.repository.HomeRepository
import java.util.TreeSet
import kotlin.random.Random

object HomeRepositoryImpl : HomeRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getAllShopItems(): List<ShopItem> {
        return try {
            val snapshot = db.collection("shopItems").get().await()
            snapshot.documents.mapNotNull { document ->
                val shopItem = document.toObject(ShopItem::class.java)?.copy(id = document.id)
                shopItem?.let {
                    Log.d("HomeRepositoryImpl", "Downloaded item: $it")
                }
                shopItem
            }
        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting documents", e)
            emptyList()
        }
    }


    override suspend fun getShopItemById(shopItemId: String): ShopItem {
        return try {
            val document = db.collection("shopItems").document(shopItemId).get().await()
            document.toObject(ShopItem::class.java)?.copy(id = document.id)
                ?: throw NoSuchElementException("Shop item with ID $shopItemId not found.")
        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting document by ID", e)
            throw e
        }
    }

    // TODO: Do async getting the data from firestore!
    override suspend fun getShopItemByAttributes(
        sortBy: SortBy,
        name: String?,
        hasDiscount: Boolean?,
        availableInStock: Boolean,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minRating: Float?
    ): List<ShopItem> {
        val snapshot = db.collection("shopItems").get().await()
        val shopItems = snapshot.documents.mapNotNull {
            it.toObject(ShopItem::class.java)?.copy(id = it.id)
        }
        return when (sortBy) {
            SortBy.PRICE_ASC -> shopItems.sortedBy { it.price }
            SortBy.PRICE_DESC -> shopItems.sortedByDescending { it.price }
            SortBy.RATING -> shopItems.sortedByDescending { it.rating }
        }

    }

}