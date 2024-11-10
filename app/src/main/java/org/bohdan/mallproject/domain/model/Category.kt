package org.bohdan.mallproject.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val name: String = "",
    val subcategoriesIds: List<String> = emptyList(),
    val id: String = ""
): Parcelable