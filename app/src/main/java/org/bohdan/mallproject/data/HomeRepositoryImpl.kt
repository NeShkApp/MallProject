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
//    private val db = Firebase.firestore

    private val db = FirebaseFirestore.getInstance()

    // TODO: Make normal LiveData with get() -shopItems
//    private val shopItemsLD = MutableLiveData<List<ShopItem>>()
//
//    private val shopItems = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})

    init {
//        val items = mutableListOf<ShopItem>()
//        for (i in 1..10) {
//            items.add(ShopItem("Product $i",
//                "Description of Product $i",
//                "Category $i",
//                i * 10.0, i.toFloat(),
//                "Image URL $i"))
//        }
//        _shopItems.value = items
    }

    //working code
//    override fun getAllShopItems(): LiveData<List<ShopItem>> {
//        val shopItemsLiveData = MutableLiveData<List<ShopItem>>()
//
//        db.collection("shopItems")
//            .get()
//            .addOnSuccessListener { result ->
//                val items = mutableListOf<ShopItem>()
//                for (document in result) {
//                    val item = document.toObject(ShopItem::class.java)
//                    items.add(item.copy(id = document.id))
//                    Log.d("HomeRepositoryImpl", "Added item =>${item.toString()}")
//                }
//                shopItemsLiveData.value = items
//                Log.d("HomeRepositoryImpl", "Fetched ${items.size} items")
//            }
//            .addOnFailureListener { exception ->
//                Log.w("HomeRepositoryImpl", "Error getting documents.", exception)
//                shopItemsLiveData.value = emptyList()
//            }
//
//        return shopItemsLiveData
//    }

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
        TODO("Not yet implemented")
    }

    override suspend fun getShopItemByAttributes(
        sortBy: SortBy,
        name: String?,
        hasDiscount: Boolean?,
        availableInStock: Boolean,
        category: String?,
        minPrice: Double?,
        maxPrice: Double?,
        minRating: Float?
    ): LiveData<List<ShopItem>> {
        TODO("Not yet implemented")
    }

}