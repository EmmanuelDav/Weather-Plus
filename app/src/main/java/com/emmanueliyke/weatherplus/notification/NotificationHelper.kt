package com.emmanueliyke.weatherplus.notification


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.emmanueliyke.weatherplus.R
import com.emmanueliyke.weatherplus.data.local.entries.CityWeatherEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@RequiresApi(Build.VERSION_CODES.O)
@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val CHANNEL_ID = "weather_channel"
        const val CHANNEL_NAME = "Weather Updates"
        const val NOTIFICATION_ID = 1001
    }

    init {
        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Hourly weather updates for your favourite city"
        }
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun showWeatherNotification(city: CityWeatherEntity) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("${city.name}, ${city.country}")
            .setContentText("${city.tempCelsius.toInt()}°C — ${city.description}")
            .setSubText("Current Weather")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.notify(NOTIFICATION_ID, notification)
    }
}