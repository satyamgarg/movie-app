package com.example.movie.presentation.movie.list.viewModel

import com.example.movie.data.response.MovieListResponse

sealed class MovieListEvents {
    object Loading : MovieListEvents()
    data class OnMovieListSuccess(val response: List<MovieListResponse.Result>?) : MovieListEvents()
    data class OnMovieListFailure(val message: String) : MovieListEvents()
}