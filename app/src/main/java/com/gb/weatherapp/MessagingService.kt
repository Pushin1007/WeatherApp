package com.gb.weatherapp

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token) //здесь отправляем полученный токен на сервер (на наш - проекта)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val custom = message.data[CUSTOM_FIELD]
        Log.i(TAG, custom ?: "no data!")// если данных не будет, высветить лог
        if (message.data.isNotEmpty()) {
            handleDataMessage(message.data.toMap())
        }
    }

    private fun handleDataMessage(data: Map<String, String>) {
        val title = data[PUSH_KEY_TITLE]
        val message = data[PUSH_KEY_MESSAGE]

    }
}