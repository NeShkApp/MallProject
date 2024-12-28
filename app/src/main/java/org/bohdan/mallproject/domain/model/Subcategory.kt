package org.bohdan.mallproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Subcategory(
    val name: String = "",
    val imageUrl: String = "",
    val categoryId: String = "",
    val id: String = "",
    val productIds: List<String> = emptyList()
): Parcelable