package com.example.movie.presentation.movie.details.viewModel

import com.example.movie.data.response.MovieDetailResponse

sealed class MovieDetailEvent {
    object Void : MovieDetailEvent()
    object Loading : MovieDetailEvent()
    data class OnMovieDetailSuccess(val response: MovieDetailResponse) : MovieDetailEvent()
    data class OnMovieDetailFailure(val message: String) : MovieDetailEvent()
}