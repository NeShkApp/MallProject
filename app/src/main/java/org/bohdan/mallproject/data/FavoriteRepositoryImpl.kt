package org.bohdan.mallproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.FavoriteRepository
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): FavoriteRepository {

    private val usersCollection = firestore.collection("users")
    private val userId = auth.currentUser?.uid

    override suspend fun addItemToFavorite(shopItemId: String) {
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val userSnapshot = userDoc.get().await()
                val userFavorite =
                    userSnapshot.get("favorite") as? MutableList<String> ?: mutableListOf()
                if (!isItemInFavorite(shopItemId)) {
                    userFavorite.add(shopItemId)
                    userDoc.update("favorite", userFavorite).await()
                }
            } catch (e: Exception) {
                throw RuntimeException("Failed to add item to favorites", e)
            }
        } else {
            throw IllegalStateException("User ID is null")
        }
    }

    override suspend fun removeItemFromFavorite(shopItemId: String) {
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                userDoc.update("favorite", FieldValue.arrayRemove(shopItemId)).await()
            } catch (e: Exception) {
                throw RuntimeException("Failed to remove item from favorites", e)
            }
        } else {
            throw IllegalStateException("User ID is null")
        }
    }

    override suspend fun isItemInFavorite(shopItemId: String): Boolean {
        if(userId != null){
            try{
                val userDoc = usersCollection.document(userId)
                val userSnapshot = userDoc.get().await()
                val favoriteItems = userSnapshot.get("favorite") as? List<String>
                return favoriteItems?.contains(shopItemId) ?: false
            }catch (e: Exception){
                throw RuntimeException("Failed to check is item in favorites: ", e)
            }
        }else{
            throw IllegalStateException("User ID is null")
        }
    }

    override suspend fun getFavoriteItems(): List<ShopItem> {
        if(userId!= null){
            try{
                val userDoc = usersCollection.document(userId)
                val userSnapshot = userDoc.get().await()

                val favoriteIds = userSnapshot.get("favorite") as? List<String> ?: emptyList()

                if (favoriteIds.isEmpty()) {
                    return emptyList()
                }

                val productsCollection = firestore.collection("products")
                return favoriteIds.mapNotNull { productId ->
                    try{
                        val productsSnapshot = productsCollection.document(productId).get().await()
                        productsSnapshot.toObject(ShopItem::class.java)?.copy(
                            id = productId
                        )
                    }catch (e: Exception){
                        throw RuntimeException("Failed to get favorite item", e)
                    }
                }
            }catch (e: Exception){
                throw RuntimeException("Failed to get favorite items: ", e)
            }
        }else{
            throw IllegalStateException("User ID is null")
        }
    }

}