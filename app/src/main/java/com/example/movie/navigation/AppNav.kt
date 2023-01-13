package com.example.movie.navigation

import android.net.Uri
import com.example.movie.ui.movie.MovieActivity
import com.example.movie.utils.Constants

sealed class AppNav(val route: String) {
    object MovieList : AppNav(route = "movie_list")
    object MovieDetail : AppNav(route = "movie_detail") {
        fun navigate(movieActivity: MovieActivity, movie: String) {
            movieActivity.navigateTo(destination = this, Constants.MOVIE_DETAILS to Uri.encode(movie))
        }
    }
}