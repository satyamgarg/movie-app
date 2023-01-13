package com.example.movie.di

import com.example.movie.api.MovieService
import com.example.movie.api.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Singleton

@InstallIn(ActivityComponent::class)
@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun provideMovieRepository(movieService: MovieService) =
        MovieRepository(movieService = movieService)

}