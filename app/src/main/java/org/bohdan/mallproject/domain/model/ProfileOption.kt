package org.bohdan.mallproject.domain.model

data class ProfileOption(
    val id: Int,
    val title: String,
    val iconResId: Int? = null
) {
    companion object {
        const val PROFILE = 1
        const val SETTINGS = 2
        const val CHANGE_LANGUAGE = 3
        const val LOGOUT = 4
    }
}