package com.emmanueliyke.weatherplus.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.emmanueliyke.weatherplus.ui.citylist.CityListScreen
import com.emmanueliyke.weatherplus.ui.navigation.NavGraph
import com.emmanueliyke.weatherplus.ui.theme.WeatherPlusTheme
import com.emmanueliyke.weatherplus.worker.WorkManagerScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionLauncher.launch(
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        }

        WorkManagerScheduler.schedule(this)

        setContent {
            WeatherPlusTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}