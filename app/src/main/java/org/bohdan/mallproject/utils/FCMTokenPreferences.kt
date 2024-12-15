package org.bohdan.mallproject.utils

import android.content.Context
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging

object FCMTokenPreferences {

    private const val PREFS_NAME = "fcm_prefs"
    private const val KEY_FCM_TOKEN = "fcm_token"

    fun saveToken(context: Context, token: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_FCM_TOKEN, token).apply()
        Log.d("FCMTokenManager", "FCM Token saved: $token")
    }

    fun getToken(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_FCM_TOKEN, null)
    }

    fun hasToken(context: Context): Boolean {
        return getToken(context) != null
    }

    fun fetchAndSaveToken(context: Context, onTokenFetched: (String) -> Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCMTokenManager", "Fetching FCM token failed", task.exception)
                return@addOnCompleteListener
            }

            val token = task.result
            saveToken(context, token)
            onTokenFetched(token)
        }
    }
}