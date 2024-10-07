package com.cinetech.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cinetech.ui.screen.FilmScreen
import com.cinetech.ui.screen.main.MainScreen
import com.cinetech.ui.screen.search.SearchScreen

@Composable
fun KinopoiskNavigationHost(
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Main
    ) {
        composable<Screen.Main> {
            MainScreen(
                onNavigate = { navHostController.navigate(it) }
            )
        }

        composable<Screen.Search> {
            SearchScreen(
                onPop = { navHostController.popBackStack() },
                onNavigate = { navHostController.navigate(it) }
            )
        }

        composable<Screen.Film> {
            FilmScreen()
        }
    }
}