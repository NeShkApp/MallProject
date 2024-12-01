package org.bohdan.mallproject.domain.usecase.order

import org.bohdan.mallproject.domain.model.ShopItem
import org.bohdan.mallproject.domain.repository.OrderRepository
import javax.inject.Inject

class AddOrderToFirestoreUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(shopItems: List<ShopItem>){
        orderRepository.addOrderToFirestore(shopItems)
    }
}