package com.emmanueliyke.weatherplus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.emmanueliyke.weatherplus.ui.citylist.CityListScreen
import com.emmanueliyke.weatherplus.ui.navigation.NavGraph
import com.emmanueliyke.weatherplus.ui.theme.WeatherPlusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherPlusTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}