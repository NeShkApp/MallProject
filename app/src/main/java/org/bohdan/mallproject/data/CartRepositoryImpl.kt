package org.bohdan.mallproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.CartRepository
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : CartRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun addItemToCart(shopItemId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val userCart = userDoc.get().await().get("cart") as? MutableMap<String, Int> ?: mutableMapOf()

                userCart[shopItemId] = (userCart[shopItemId] ?: 0) + 1

                userDoc.update("cart", userCart).await()
            } catch (e: Exception) {
                println("Error adding item to cart: ${e.message}")
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
                userDoc.update("cart.$shopItemId", FieldValue.delete()).await()
            } catch (e: Exception) {
                println("Error removing item from cart: ${e.message}")
            }
        }
    }


    // TODO: add stockQuantity check due to getting cart items.
    override suspend fun getCartItems(): List<ShopItem> {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val cartMap = userDoc.get().await().get("cart") as? Map<String, Int> ?: return emptyList()

                val productsCollection = firestore.collection("products")
                return cartMap.mapNotNull { (productId, quantity) ->
                    try {
                        val productSnapshot = productsCollection.document(productId).get().await()
                        productSnapshot.toObject(ShopItem::class.java)?.copy(
                            id = productId,
                            selectedQuantity = quantity
                        )
                    } catch (e: Exception) {
                        println("Error fetching product with ID $productId: ${e.message}")
                        null
                    }
                }
            } catch (e: Exception) {
                println("Error fetching cart items: ${e.message}")
            }
        }
        return emptyList()
    }



    override suspend fun isItemInCart(shopItemId: String): Boolean {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                val documentSnapshot = userDoc.get().await()
                val cartItems = documentSnapshot.get("cart") as? Map<String, Int>
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

    override suspend fun updateSelectedQuantity(shopItemId: String, quantity: Int) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val userDoc = usersCollection.document(userId)
                userDoc.update("cart.$shopItemId", quantity).await()
            } catch (e: Exception) {
                println("Error updating item quantity: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
    }

    override suspend fun calculateTotalPrice(): Double {
        val userId = auth.currentUser?.uid
        if(userId != null){
            try{
                var totalPrice = 0.0
                val items = getCartItems()
                items.forEach{
                    totalPrice+= it.price * it.selectedQuantity
                }
                return totalPrice
            }catch (e: Exception){
                println("Error calculating total amount: ${e.message}")
            }
        }else{
            println("User not authenticated")

        }
        return 0.0
    }

}