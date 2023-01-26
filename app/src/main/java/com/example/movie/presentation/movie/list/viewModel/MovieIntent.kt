package com.example.movie.presentation.movie.list.viewModel

sealed class MovieIntent {
    object GetMovies : MovieIntent()
    data class GetMovieDetails(val movieId : Int) : MovieIntent()
}