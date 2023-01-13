package com.example.movie.ui.movie.details.viewModel

import com.example.movie.data.MovieDetailResponse

sealed class MovieDetailEvent {
    object Void : MovieDetailEvent()
    object Loading : MovieDetailEvent()
    data class OnMovieDetailSuccess(val response: MovieDetailResponse) : MovieDetailEvent()
    data class OnMovieDetailFailure(val message: String) : MovieDetailEvent()
}