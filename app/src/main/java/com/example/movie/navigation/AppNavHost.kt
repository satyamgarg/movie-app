package com.example.movie.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movie.ui.movie.details.MovieDetailScreen
import com.example.movie.ui.movie.list.MovieListScreen
import com.example.movie.utils.Constants

@Composable
fun AppNavHost(modifier: Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = AppNav.MovieList.route
    ) {
        composable(route = AppNav.MovieList.route) {
            MovieListScreen()
        }

        composable(
            route = AppNav.MovieDetail.route + getArgsString(
                Constants.MOVIE_DETAILS
            ),
            arguments = listOf(navArgument(Constants.MOVIE_DETAILS) {
                type = NavType.StringType
            })
        ) {
            MovieDetailScreen(movieDetails = Uri.decode(it.arguments?.getString(Constants.MOVIE_DETAILS)).orEmpty()) {
                navController.navigateUp()
            }
        }
    }
}

private fun getArgsString(vararg argKey: String) = "?${
    argKey.joinToString(separator = "&") {
        "${it}={${it}}"
    }
}"