package org.bohdan.mallproject.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
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
        val snapshot = db.collection("products").get().await()
        val shopItems = snapshot.documents.mapNotNull {
            it.toObject(ShopItem::class.java)?.copy(id = it.id)
        }
        return when (sortBy) {
            SortBy.PRICE_ASC -> shopItems.sortedBy { it.price }
            SortBy.PRICE_DESC -> shopItems.sortedByDescending { it.price }
            SortBy.RATING -> shopItems.sortedByDescending { it.rating }
        }

    }

    override suspend fun getSubcategoriesByCategory(category: Category): List<Subcategory> {
        return try {
            val subcategoryIds = category.subcategoriesIds

            Log.d("CategoryRepository", "Fetching subcategories with IDs: $subcategoryIds")

            // Завантажуємо документи по ID підкатегорій
            val subcategoryDocs = subcategoryIds.map { subcategoryId ->
                db.collection("subcategories")
                    .document(subcategoryId)
                    .get()
                    .await()
            }

            // Перетворюємо документи на об'єкти Subcategory
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

            // Логуємо отриманий список підкатегорій
            Log.d("HomeRepositoryImpl", "Total fetched subcategories: ${subcategories.size}")
            subcategories

        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting subcategories: ${e.message}")
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

            categories.forEach { category ->
                Log.d("Category Info", "Category ID: ${category.id}, Name: ${category.name}, Subcategories IDs: ${category.subcategoriesIds}")
            }

            categories
        } catch (e: Exception) {
            Log.e("HomeRepositoryImpl", "Error getting documents", e)
            emptyList()
        }
    }

}