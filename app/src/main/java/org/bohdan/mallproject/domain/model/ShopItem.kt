package org.bohdan.mallproject.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopItem(
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val rating: Float = 0f,
    val imageUrl: String = "",
    val id: String = "",
    val quantityInStock: Int = 0,
    @get: Exclude
    var selectedQuantity: Int = 1
): Parcelable



