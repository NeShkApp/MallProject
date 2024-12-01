package org.bohdan.mallproject.domain.model

data class Order(
    val orderId: String = "",
    val userId: String = "",
    val productsWithQuantities: List<Map<String, Any>>,
    val totalAmount: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis()
)
