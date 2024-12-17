package org.bohdan.mallproject.domain.model

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Comment(
    val id: String = "",
    val usernameId: String = "",
    val productId: String = "",
    val text: String = "",
    val rating: Float = 0f,
    val timestamp: String = "",
    @get: Exclude
    val username: String = "User"
):Parcelable
