package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.model.ShopItem

interface OrderRepository {
    suspend fun addOrderToFirestore(shopItems: List<ShopItem>)
    suspend fun getOrdersFromFirestore(): List<Order>
    fun formatTimestamp(timestamp: Long): String
}