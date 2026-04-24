package com.emmanueliyke.weatherplus.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

object WorkManagerScheduler {

    private const val WORK_NAME = "weather_hourly_notification"

    fun schedule(context: Context) {
        val initialDelay = minutesUntilNextHour()

        val request = PeriodicWorkRequestBuilder<WeatherNotificationWorker>(
            15, TimeUnit.MINUTES
        )
            .setInitialDelay(initialDelay, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun minutesUntilNextHour(): Long {
        val calendar = Calendar.getInstance()
        val minutesPast = calendar.get(Calendar.MINUTE)
        return if (minutesPast == 0) 60L else (60 - minutesPast).toLong()
    }
}