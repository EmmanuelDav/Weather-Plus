package com.emmanueliyke.weatherplus.ui.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.emmanueliyke.weatherplus.ui.citydetail.CityDetailScreen
import com.emmanueliyke.weatherplus.ui.citylist.CityListScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.CityList
    ) {
        composable<NavRoutes.CityList> {
            CityListScreen(
                onCityClick = { cityId ->
                    navController.navigate(NavRoutes.CityDetail(cityId = cityId))
                }
            )
        }

        composable<NavRoutes.CityDetail> {
            CityDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}