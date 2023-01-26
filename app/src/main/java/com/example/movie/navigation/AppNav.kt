package com.example.movie.navigation

sealed class AppNav(val route: String) {
    object MovieList : AppNav(route = "movie_list")
    object MovieDetail : AppNav(route = "movie_detail")
}