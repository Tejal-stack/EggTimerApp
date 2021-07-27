package com.rl.eggtimerapp.util

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService


fun Context.getNotificationManager(): NotificationManager = getSystemService(
    this,
    NotificationManager::class.java
) as NotificationManager