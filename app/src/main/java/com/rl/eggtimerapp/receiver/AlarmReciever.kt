package com.rl.eggtimerapp.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rl.eggtimerapp.util.getNotificationManager
import com.rl.eggtimerapp.util.sendNotification

class AlarmReciever :BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
       context.getNotificationManager().sendNotification(

           "It's a bon Appetite",
           context

       )
    }
}