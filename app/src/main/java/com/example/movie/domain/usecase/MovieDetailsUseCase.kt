package com.example.movie.domain.usecase

import com.example.movie.data.repository.MovieRepository
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.presentation.movie.details.viewModel.MovieDetailEvent
import com.example.movie.utils.ResultType
import javax.inject.Inject

open class MovieDetailsUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getMovieDetails(movieId : Int): MovieDetailEvent {
        return when (val response = movieRepository.getMovieDetails(movieId = movieId)) {
            is ResultType.Success -> handleMoviesSuccessResponse(response.value)
            is ResultType.Error -> handleMoviesErrorResponse(response.errorMessage)
        }
    }

    private fun handleMoviesErrorResponse(message: String) = MovieDetailEvent.OnMovieDetailFailure(message)

    private fun handleMoviesSuccessResponse(movieDetailResponse: MovieDetailResponse) =
        MovieDetailEvent.OnMovieDetailSuccess(movieDetailResponse)
}

