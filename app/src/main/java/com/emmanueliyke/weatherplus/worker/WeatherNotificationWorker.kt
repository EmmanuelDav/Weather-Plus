package com.emmanueliyke.weatherplus.worker


import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emmanueliyke.weatherplus.data.local.dao.CityWeatherDao
import com.emmanueliyke.weatherplus.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val dao: CityWeatherDao,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, params) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        return try {
            val favorite = dao.getTopFavorite()
                ?: return Result.success()
            notificationHelper.showWeatherNotification(favorite)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}