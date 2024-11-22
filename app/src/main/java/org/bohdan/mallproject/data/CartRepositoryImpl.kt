package org.bohdan.mallproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.CartRepository

class CartRepositoryImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CartRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun addItemToCart(shopItemId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                userDoc.update("cart", FieldValue.arrayUnion(shopItemId)).await()
            } catch (e: FirebaseFirestoreException) {
                println("Error adding item to cart: ${e.message}")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
    }

    override suspend fun removeItemFromCart(shopItemId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                userDoc.update("cart", FieldValue.arrayRemove(shopItemId)).await()
            } catch (e: FirebaseFirestoreException) {
                println("Error removing item from cart: ${e.message}")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
    }

    override suspend fun isItemInCart(shopItemId: String): Boolean {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val documentSnapshot = userDoc.get().await()
                val cartItems = documentSnapshot.get("cart") as? List<String>
                return cartItems?.contains(shopItemId) ?: false
            } catch (e: FirebaseFirestoreException) {
                println("Error checking item in cart: ${e.message}")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
        return false
    }

    override suspend fun getCartItems(): List<ShopItem> {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val documentSnapshot = userDoc.get().await()
                val cartItemsIds = documentSnapshot.get("cart") as? List<String>

                if (!cartItemsIds.isNullOrEmpty()) {
                    val productsCollection = firestore.collection("products")
                    val items = cartItemsIds.mapNotNull { itemId ->
                        try {
                            val productSnapshot = productsCollection.document(itemId).get().await()
                            productSnapshot.toObject(ShopItem::class.java)?.copy(id = productSnapshot.id)
                        } catch (e: Exception) {
                            println("Error fetching product with ID $itemId: ${e.message}")
                            null
                        }
                    }
                    return items
                }
            } catch (e: FirebaseFirestoreException) {
                println("Error fetching cart items: ${e.message}")
            } catch (e: Exception) {
                println("Unexpected error: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
        return emptyList()
    }

}
