package org.bohdan.mallproject.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.Comment
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class ShopItemDetailsRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
): ShopItemDetailsRepository {

    override suspend fun getShopItemDetailsById(shopItemId: String): ShopItem {
        return try {
            val document = firestore.collection("products").document(shopItemId).get().await()
            if (!document.exists()) {
                throw NoSuchElementException("Shop item with ID $shopItemId not found.")
            }
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

//    override suspend fun getShopItemComments(shopItemId: String): List<Comment> {
//        try{
//            val comments = listOf(
//                Comment(
//                    id = "1",
//                    usernameId = "user1",
//                    productId = shopItemId,
//                    text = "Great product! Highly recommend.",
//                    rating = 4.5f
//                ),
//                Comment(
//                    id = "2",
//                    usernameId = "user2",
//                    productId = shopItemId,
//                    text = "Not bad, but could be improved.",
//                    rating = 3.0f
//                ),
//                Comment(
//                    id = "3",
//                    usernameId = "user3",
//                    productId = shopItemId,
//                    text = "Worst purchase ever. Totally regret.",
//                    rating = 1.0f
//                )
//            )
//            return comments
//        }catch (e: Exception){
//            println("Error fetching comments items: ${e.message}")
//        }
//        return emptyList()
//    }

    override suspend fun getShopItemComments(shopItemId: String): List<Comment> {
        return try {
            val documents = firestore.collection("reviews")
                .whereEqualTo("productId", shopItemId)
                .get()
                .await()

            // Отримати відгуки
            val comments = documents.mapNotNull {
                it.toObject(Comment::class.java).copy(id = it.id)
            }

            // Для кожного коментаря отримати name користувача
            val commentsWithUsernames = comments.map { comment ->
                val userDocument = firestore.collection("users")
                    .document(comment.usernameId)
                    .get()
                    .await()

                val username = userDocument.getString("name") ?: "Unknown User"
                comment.copy(username = username)
            }.sortedByDescending { it.timestamp }

            commentsWithUsernames
        } catch (e: Exception) {
            Log.e("Firestore", "Error fetching comments: ${e.message}")
            emptyList()
        }
    }

    override suspend fun hasUserPurchasedProduct(shopItemId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false
        return try {
            val ordersSnapshot = firestore.collection("users")
                .document(userId)
                .collection("orders")
                .get()
                .await()

            // Перевіряємо, чи є productId у масиві productsWithQuantities
            for (order in ordersSnapshot.documents) {
                val productsWithQuantities = order["productsWithQuantities"] as? List<Map<String, Any>>
                productsWithQuantities?.forEach { product ->
                    if (product["shopItemId"] == shopItemId) {
                        return true
                    }
                }
            }

            false
        } catch (e: Exception) {
            Log.e("Firestore", "Error checking purchased product: ${e.message}")
            false
        }
    }


    override suspend fun hasUserAlreadyReviewed(shopItemId: String): Boolean {
        val userId = auth.currentUser?.uid ?: return false

        val reviews = firestore.collection("reviews")
            .whereEqualTo("usernameId", userId)
            .whereEqualTo("productId", shopItemId)
            .get()
            .await()

        return reviews.documents.isNotEmpty()
    }

    override suspend fun canUserLeaveComment(shopItemId: String): Boolean {
        val hasPurchased = hasUserPurchasedProduct(shopItemId)
        val hasReviewed = hasUserAlreadyReviewed(shopItemId)
        return hasPurchased && !hasReviewed
    }

    override suspend fun submitReview(shopItemId: String, rating: Float, text: String) {
        val usernameId = auth.currentUser?.uid
        try {
            val timestamp = System.currentTimeMillis().toString()

            val newComment = hashMapOf(
                "usernameId" to usernameId,
                "productId" to shopItemId,
                "text" to text,
                "rating" to rating,
                "timestamp" to timestamp
            )

            firestore.collection("reviews")
                .add(newComment)
                .await()

        } catch (e: Exception) {
            throw Exception("Error submitting review: ${e.message}")
        }
    }


}