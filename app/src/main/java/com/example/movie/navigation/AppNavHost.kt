package com.example.movie.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.movie.presentation.movie.details.MovieDetailScreen
import com.example.movie.presentation.movie.list.MovieListScreen
import com.example.movie.utils.Constants
import com.google.gson.Gson

@Composable
fun AppNavHost(modifier: Modifier, navController: NavHostController) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = AppNav.MovieList.route
    ) {
        composable(route = AppNav.MovieList.route) {
            MovieListScreen {
                navController.navigate(
                    AppNav.MovieDetail.route + Constants.OPR_QUESTION + getArgs(
                        Constants.MOVIE_DETAILS to Uri.encode(
                            Gson().toJson(it)
                        )
                    )
                )
            }
        }

        composable(
            route = AppNav.MovieDetail.route + getArgsString(
                Constants.MOVIE_DETAILS
            ),
            arguments = listOf(navArgument(Constants.MOVIE_DETAILS) {
                type = NavType.StringType
            })
        ) {
            MovieDetailScreen(
                movieDetails = Uri.decode(it.arguments?.getString(Constants.MOVIE_DETAILS))
                    .orEmpty()
            ) {
                navController.navigateUp()
            }
        }
    }
}

private fun getArgsString(vararg argKey: String) =
    Constants.OPR_QUESTION + argKey.joinToString(separator = Constants.OPR_AND) {
        "${it}={${it}}"
    }

private fun getArgs(vararg args: Pair<String, Any>): String {
    return args.joinToString(Constants.OPR_AND) {
        "${it.first}=${it.second}"
    }
}