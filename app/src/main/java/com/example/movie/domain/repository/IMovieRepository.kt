package com.example.movie.domain.repository

import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.data.response.MovieListResponse
import com.example.movie.utils.ResultType

interface IMovieRepository {
    suspend fun getPopularMovies() : ResultType<MovieListResponse>
    suspend fun getMovieDetails(movieId: Int): ResultType<MovieDetailResponse>
}