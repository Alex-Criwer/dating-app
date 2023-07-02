package com.example.sonder_dating_app.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class NotificationService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }

    override fun onNewToken(p0: String) {
        Log.e("a", "asca new message token : $p0")
        super.onNewToken(p0)
    }
}
