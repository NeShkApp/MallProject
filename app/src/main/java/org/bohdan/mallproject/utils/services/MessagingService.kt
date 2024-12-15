package org.bohdan.mallproject.utils.services

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.bohdan.mallproject.R
import org.bohdan.mallproject.presentation.ui.auth.AuthActivity
import org.bohdan.mallproject.presentation.ui.home.MainActivity

//@SuppressLint("MissingFirebaseInstanceTokenRefresh")
//class MessagingService : FirebaseMessagingService() {
//
//    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        // Якщо це "notification" тип повідомлення
//        remoteMessage.notification?.let {
//            val channelId = getChannelId(remoteMessage)
//            sendNotification(it.title, it.body, channelId)
//        }
//    }
//
//    private fun getChannelId(remoteMessage: RemoteMessage): String {
//        // Перевіряємо додаткові дані "channel" в payload
//        return remoteMessage.data["channel"] ?: "default_channel"
//    }
//
//    private fun sendNotification(title: String?, message: String?, channelId: String) {
//        // Створюємо Notification Channel (для Android 8.0+)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            val channelName = when (channelId) {
//                "promo_channel" -> "Promotions"
//                "news_channel" -> "News Updates"
//                else -> "General Notifications"
//            }
//            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        // Створюємо Intent для MainActivity
//        val intent = Intent(this, MainActivity::class.java).apply {
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//        }
//        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
//
//        // Побудова повідомлення з правильним ChannelId
//        val notificationBuilder = NotificationCompat.Builder(this, channelId)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setSmallIcon(R.drawable.ic_notifications)
//            .setContentIntent(pendingIntent)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        // Відправка повідомлення
//        val notificationManager = NotificationManagerCompat.from(this)
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.POST_NOTIFICATIONS
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            return
//        }
//        notificationManager.notify(channelId.hashCode(), notificationBuilder.build())
//    }
//}

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val channelId = getChannelId(remoteMessage)
            sendNotification(it.title, it.body, channelId)
        }
    }

    private fun getChannelId(remoteMessage: RemoteMessage): String {
        return remoteMessage.data["channel"] ?: "default_channel"
    }

    private fun sendNotification(title: String?, message: String?, channelId: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channelName = when (channelId) {
                "promo_channel" -> "Promotions"
                "news_channel" -> "News"
                else -> "General Notifications"
            }
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Відправка повідомлення
        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(channelId.hashCode(), notificationBuilder.build())
    }
}

