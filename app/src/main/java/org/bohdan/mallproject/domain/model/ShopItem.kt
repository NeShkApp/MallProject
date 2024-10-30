package org.bohdan.mallproject.domain.model

//FOR END VERSION OF APP
//data class ShopItem(
//    val id: Int,
//    val name: String,
//    val quantity: Int,
//    val description: String,
//    val category: String,
//    val price: Double,
//    val discount: Double? = null,
//    val rating: Float,
//    val reviewCount: Int,
//    val imageUrl: String
//)

//FOR TEMPORARY USING
data class ShopItem(
    val name: String = "",
    val description: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val id: String = ""
)



