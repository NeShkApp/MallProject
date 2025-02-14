package org.bohdan.mallproject.domain.model

data class ProfileOption(
    val id: Int,
    val titleResId: Int,
    val iconResId: Int? = null,
    val title: String = "",
) {
    companion object {
//        const val PROFILE = 1
        const val THEME = 2
        const val CHANGE_LANGUAGE = 3
        const val ORDERS = 4
        const val LOGOUT = 5
    }
}