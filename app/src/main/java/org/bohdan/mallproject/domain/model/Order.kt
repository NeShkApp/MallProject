package org.bohdan.mallproject.domain.model

import com.google.firebase.firestore.Exclude

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val productsWithQuantities: List<Map<String, Any>> = emptyList(),
    val totalAmount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    @get: Exclude
    val shopItems: List<ShopItem> = emptyList(),
    val formattedTimestamp: String = ""
)
