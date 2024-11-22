package org.bohdan.mallproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.Category
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.model.SortBy
import org.bohdan.mallproject.domain.model.Subcategory
import org.bohdan.mallproject.domain.repository.HomeRepository
import java.util.TreeSet
import kotlin.random.Random

object HomeRepositoryImpl : HomeRepository {
    private val db = FirebaseFirestore.getInstance()

    override suspend fun getAllShopItems(): List<ShopItem> {
        return try {
            val snapshot = db.collection("products").get().await()
            snapshot.documents.mapNotNull { document ->
                document.toObject(ShopItem::class.java)?.copy(id = document.id)
            }
        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting documents", e)
            emptyList()
        }
    }

//    override suspend fun getShopItemById(shopItemId: String): ShopItem {
//        return try {
//            val document = db.collection("shopItems").document(shopItemId).get().await()
//            document.toObject(ShopItem::class.java)?.copy(id = document.id)
//                ?: throw NoSuchElementException("Shop item with ID $shopItemId not found.")
//        } catch (e: Exception) {
//            Log.e("HomeRepositoryImpl", "Error getting document by ID", e)
//            throw e
//        }
//    }

    override suspend fun getSubcategoriesByCategory(category: Category): List<Subcategory> {
        return try {
            val subcategoryIds = category.subcategoriesIds

            Log.d("CategoryRepository", "Fetching subcategories with IDs: $subcategoryIds")

            val subcategoryDocs = subcategoryIds.map { subcategoryId ->
                db.collection("subcategories")
                    .document(subcategoryId)
                    .get()
                    .await()
            }

            val subcategories = subcategoryDocs.mapNotNull { document ->
                if (document.exists()) {
                    val subcategory = document.toObject(Subcategory::class.java)?.copy(id = document.id)
                    Log.d("HomeRepositoryImpl", "Fetched subcategory: $subcategory")
                    subcategory
                } else {
                    Log.d("HomeRepositoryImpl", "Document with ID ${document.id} does not exist")
                    null
                }
            }

            Log.d("HomeRepositoryImpl", "Total fetched subcategories: ${subcategories.size}")
            subcategories

        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting subcategories: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getShopItemsByFilters(
        category: Category?,
        subcategory: Subcategory?,
        searchQuery: String?,
    ): List<ShopItem> {
        return try {
            val actualQuery = if (searchQuery.isNullOrEmpty()) null else searchQuery

            val shopItems = subcategory?.let {
                val productIds = subcategory.productIds
                val query = db.collection("products")
                    .whereIn(FieldPath.documentId(), productIds)
                val snapshot = query.get().await()

                snapshot.documents.mapNotNull { document ->
                    document.toObject(ShopItem::class.java)?.copy(id = document.id)
                }
            } ?: category?.let { cat ->
                val subcategories = getSubcategoriesByCategory(cat)
                val allProductIds = subcategories.flatMap { it.productIds }
                val query = db.collection("products")
                    .whereIn(FieldPath.documentId(), allProductIds)
                val snapshot = query.get().await()

                snapshot.documents.mapNotNull { document ->
                    document.toObject(ShopItem::class.java)?.copy(id = document.id)
                }
            } ?: getAllShopItems()

            shopItems.forEach{
                Log.d("HomeRepositoryImpl", "getShopItemsByFilters: ${it.toString()}")
            }

            actualQuery?.let { query ->
                shopItems.filter { it.name.contains(query, ignoreCase = true) }
            } ?: shopItems

        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "getShopItemsByFilters: ", e)
            emptyList()
        }
    }

    override suspend fun getAllCategories(): List<Category> {
        return try {
            val categories = db.collection("categories").get().await()
                .documents.map { document ->
                document.toObject(Category::class.java)?.copy(id = document.id)
                    ?: throw NoSuchElementException("Category with ID ${document.id} not found.")
            }
            categories
        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting documents", e)
            emptyList()
        }
    }

    override fun sortShopItems(
        items: List<ShopItem>,
        sortBy: SortBy?
    ): List<ShopItem> {
        return when (sortBy) {
            SortBy.PRICE_ASC -> items.sortedBy { it.price }
            SortBy.PRICE_DESC -> items.sortedByDescending { it.price }
            SortBy.RATING -> items.sortedByDescending { it.rating }
            else -> items
        }
    }


}