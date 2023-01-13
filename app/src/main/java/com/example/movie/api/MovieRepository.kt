package com.example.movie.api

import com.example.movie.utils.safeApiCall
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {
    suspend fun getPopularMovies(apiKey: String, language: String, page: Long) =
        safeApiCall {
            movieService.getPopularMovies(apiKey = apiKey, language = language, page = page)
        }

    suspend fun getMovieDetails(apiKey: String, language: String, movieId: Int) =
        safeApiCall {
            movieService.getMovieDetails(apiKey = apiKey, language = language, movieId = movieId)
        }
}