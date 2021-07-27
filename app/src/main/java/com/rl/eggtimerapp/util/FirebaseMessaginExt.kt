package com.rl.eggtimerapp.util

import android.content.Context
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging


const val BREAKFAST_TOPIC = "breakfast"

fun FirebaseMessaging.subscribeToTopicBreakfast(context: Context) {

    subscribeToTopic(BREAKFAST_TOPIC)
        .addOnCompleteListener {
            val userfeedback = if (it.isSuccessful)
                "Subscribed to topic"
            else
                "Failed to subscribe the topic"
            Toast.makeText(context.applicationContext, userfeedback, Toast.LENGTH_SHORT).show()
        }


}