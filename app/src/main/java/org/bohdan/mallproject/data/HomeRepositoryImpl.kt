package org.bohdan.mallproject.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.repository.HomeRepository
import java.util.TreeSet
import kotlin.random.Random

object HomeRepositoryImpl: HomeRepository {
    private val firestore = FirebaseFirestore.getInstance()

    // TODO: Make normal LiveData with get() -shopItems
//    private val shopItemsLD = MutableLiveData<List<ShopItem>>()
//
//    private val shopItems = sortedSetOf<ShopItem>({o1, o2 -> o1.id.compareTo(o2.id)})


    private val _shopItems = MutableLiveData<List<ShopItem>>()

    private var autoIncrementId = 0

    init {
        val items = mutableListOf<ShopItem>()
        for (i in 1..10) {
            items.add(ShopItem("Product $i",
                "Description of Product $i",
                "Category $i",
                i * 10.0, i.toFloat(),
                "Image URL $i"))
        }
        _shopItems.value = items
    }

    override fun getAllShopItems(): LiveData<List<ShopItem>> {
        return _shopItems
    }

    override suspend fun getShopItemById(shopItemId: String): ShopItem {
        TODO("Not yet implemented")
    }

//    override suspend fun getAllShopItems(): List<ShopItem> {
//        val result = firestore.collection("shopItems").get().await()
//        return result.documents.mapNotNull {
//            it.toObject(ShopItem::class.java)
//        }
//    }

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