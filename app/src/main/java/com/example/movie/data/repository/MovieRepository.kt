package com.example.movie.data.repository

import com.example.movie.data.datasource.remote.MovieService
import com.example.movie.domain.repository.IMovieRepository
import com.example.movie.utils.safeApiCall
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) :
    IMovieRepository {
    override suspend fun getPopularMovies() =
        safeApiCall {
            movieService.getPopularMovies()
        }

    override suspend fun getMovieDetails(movieId: Int) =
        safeApiCall {
            movieService.getMovieDetails(movieId = movieId)
        }
}