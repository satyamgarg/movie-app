package com.example.movie.di

import com.example.movie.BuildConfig
import com.example.movie.domain.repository.IMovieRepository
import com.example.movie.data.repository.MovieRepository
import com.example.movie.data.datasource.remote.MovieService
import com.example.movie.domain.usecase.MovieDetailsUseCase
import com.example.movie.domain.usecase.MovieListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    private fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor(HttpLoggingInterceptor.Logger.DEFAULT)
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(provideLoggingInterceptor())
                }
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASEURL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    fun provideMovieRepository(movieService: MovieService): IMovieRepository {
        return MovieRepository(movieService)
    }

    @Provides
    fun provideMovieListUseCase(movieRepository: MovieRepository): MovieListUseCase {
        return MovieListUseCase(movieRepository)
    }

    @Provides
    fun provideMovieDetailsUseCase(movieRepository: MovieRepository): MovieDetailsUseCase {
        return MovieDetailsUseCase(movieRepository)
    }
}