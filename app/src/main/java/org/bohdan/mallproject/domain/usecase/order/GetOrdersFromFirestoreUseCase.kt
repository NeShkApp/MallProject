package org.bohdan.mallproject.domain.usecase.order

import org.bohdan.mallproject.domain.model.Order
import org.bohdan.mallproject.domain.repository.OrderRepository
import javax.inject.Inject

class GetOrdersFromFirestoreUseCase @Inject constructor(
    private val orderRepository: OrderRepository
) {
    suspend operator fun invoke(): List<Order>{
        return orderRepository.getOrdersFromFirestore()
    }
}