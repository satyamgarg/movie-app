package com.example.movie.data.datasource.remote

import com.example.movie.BuildConfig
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.data.response.MovieListResponse
import com.example.movie.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = BuildConfig.APIKEY,
        @Query("language") language: String = Constants.LANGUAGE,
        @Query("page") page: Long = 1
    ): Response<MovieListResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.APIKEY,
        @Query("language") language: String = Constants.LANGUAGE,
    ): Response<MovieDetailResponse>
}