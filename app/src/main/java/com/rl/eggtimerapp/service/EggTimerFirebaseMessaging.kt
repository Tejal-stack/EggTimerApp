package com.rl.eggtimerapp.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.rl.eggtimerapp.util.getNotificationManager
import com.rl.eggtimerapp.util.sendNotification

class EggTimerFirebaseMessaging : FirebaseMessagingService() {

    override fun onNewToken(newToken: String) {
        Log.i("Token", newToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        with(remoteMessage) {

            from?.run { Log.d("Message", "$this") }
            data.takeIf { it.isNotEmpty() }?.run {

                Log.d("Message", "$data")
            }

            notification?.body?.run {
                applicationContext.getNotificationManager().sendNotification(

                    this,
                    applicationContext
                )
            }


        }


    }


}