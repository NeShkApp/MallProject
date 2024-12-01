package org.bohdan.mallproject.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.OrderRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
): OrderRepository {
    override suspend fun addOrderToFirestore(shopItems: List<ShopItem>) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val orderId = firestore.collection("users")
                    .document(userId)
                    .collection("orders")
                    .document().id
                val totalAmount = shopItems.sumOf { it.price * it.selectedQuantity }

                val productsWithQuantities = shopItems.filter { it.selectedQuantity > 0 }
                    .map {
                        mapOf("shopItemId" to it.id, "quantity" to it.selectedQuantity)
                    }

                val order = Order(
                    orderId,
                    userId,
                    productsWithQuantities,
                    totalAmount,
                    System.currentTimeMillis()
                )

                val orderRef = firestore.collection("users")
                    .document(userId)
                    .collection("orders")
                    .document(orderId)

                orderRef.set(order).await()

            } catch (e: Exception) {
                println("Error adding items to order: ${e.message}")
            }
        } else {
            println("User not authenticated")
        }
    }

}