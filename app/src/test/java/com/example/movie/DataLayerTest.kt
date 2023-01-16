package com.example.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.ExperimentalComposeApi
import com.example.movie.data.repository.MovieRepository
import com.example.movie.data.datasource.remote.MovieService
import com.example.movie.data.response.MovieListResponse
import com.example.movie.utils.ResultType
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.net.UnknownHostException

@ExperimentalComposeApi
@RunWith(MockitoJUnitRunner::class)
class DataLayerTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private lateinit var movieRepository: MovieRepository
    private val service = mockk<MovieService>()

    @Before
    fun setUp() {
        movieRepository = MovieRepository(service)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successTest() {
        val movieListResponse = mockk<MovieListResponse>()
        val response = mockk<Response<MovieListResponse>>()
        every {
            response.body()
        } returns movieListResponse

        every {
            response.isSuccessful
        } returns true

        coEvery {
            service.getPopularMovies()
        } returns response

        runTest {
            val result = movieRepository.getPopularMovies()
            assert(result is ResultType.Success)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun errorTest() {
        val response = mockk<Response<MovieListResponse>>()
        every {
            response.isSuccessful
        } returns false

        coEvery {
            service.getPopularMovies()
        } returns response

        runTest {
            val result = movieRepository.getPopularMovies()
            assert(result is ResultType.Error)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun errorEmptyBodyTest() {
        val response = mockk<Response<MovieListResponse>>()
        every {
            response.isSuccessful
        } returns true

        every {
            response.body()
        } returns null

        coEvery {
            service.getPopularMovies()
        } returns response

        runTest {
            val result = movieRepository.getPopularMovies()
            assert(result is ResultType.Error)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun networkFailureTest() {
        coEvery {
            service.getPopularMovies()
        } throws UnknownHostException()

        runTest {
            val result = movieRepository.getPopularMovies()
            assert(result is ResultType.Error)
        }
    }

}