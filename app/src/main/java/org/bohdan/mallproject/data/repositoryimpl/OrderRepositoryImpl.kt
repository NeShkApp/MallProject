package org.bohdan.mallproject.data.repositoryimpl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.OrderRepository
import org.bohdan.mallproject.domain.repository.ShopItemDetailsRepository
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val shopItemDetailsRepository: ShopItemDetailsRepository
): OrderRepository {
    override suspend fun addOrderToFirestore(shopItems: List<ShopItem>) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            try {
                val orderId = firestore.collection("users")
                    .document(userId)
                    .collection("orders")
                    .document().id

//                val totalAmount = shopItems.sumOf { it.price * it.selectedQuantity }
                val totalAmount = shopItems.sumOf { item ->
                    val finalPrice = if (item.discount > 0) {
                        item.price - (item.price * item.discount / 100)
                    } else {
                        item.price
                    }
                    finalPrice * item.selectedQuantity
                }

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

    override suspend fun getOrdersFromFirestore(): List<Order> {
        val userId = auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
        return try {
            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("orders")
                .get()
                .await()

            val orders = snapshot.documents.mapNotNull { it.toObject(Order::class.java) }

            orders.map { order ->
                val productsWithDetails = order.productsWithQuantities.map { productData ->
                    val shopItemId = productData["shopItemId"].toString()
                    val selectedQuantity = productData["quantity"] as? Long ?: 0

                    // Otrzymujemy szczegóły produktu
                    val shopItem = shopItemDetailsRepository.getShopItemDetailsById(shopItemId)

                    // Dodajemy ilość
                    shopItem.copy(selectedQuantity = selectedQuantity.toInt())
                }

                // Formatujemy datę i zwracamy zaktualizowane zamówienie
                order.copy(
                    shopItems = productsWithDetails,
                    formattedTimestamp = formatTimestamp(order.timestamp)
                )
            }.sortedByDescending { it.timestamp }
        } catch (e: Exception) {
            Log.e("OrdersRepository", "Error fetching orders with details: ${e.message}")
            emptyList()
        }
    }

    override fun formatTimestamp(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMMM yyyy, HH:mm", java.util.Locale.getDefault())
        return sdf.format(java.util.Date(timestamp))
    }


}