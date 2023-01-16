package com.example.movie.domain.usecase

import com.example.movie.data.repository.MovieRepository
import com.example.movie.data.response.MovieListResponse
import com.example.movie.presentation.movie.list.viewModel.MovieListEvents
import com.example.movie.utils.ResultType
import javax.inject.Inject

open class MovieListUseCase @Inject constructor(private val movieRepository: MovieRepository) {

    suspend fun getPopularMovies(): MovieListEvents {
        return when (val response = movieRepository.getPopularMovies()) {
            is ResultType.Success -> handleMovieResponse(response.value)
            is ResultType.Error -> handleErrorResponse(response.errorMessage)
        }
    }

    private fun handleErrorResponse(message: String) = MovieListEvents.OnMovieListFailure(message)

    private fun handleMovieResponse(movieList: MovieListResponse) =
        MovieListEvents.OnMovieListSuccess(movieList.results)
}

