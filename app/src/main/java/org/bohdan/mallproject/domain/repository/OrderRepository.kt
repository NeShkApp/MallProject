package org.bohdan.mallproject.domain.repository

import org.bohdan.mallproject.domain.model.ShopItem

interface OrderRepository {
    suspend fun addOrderToFirestore(shopItems: List<ShopItem>)
}