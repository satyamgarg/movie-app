package com.example.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movie.data.response.MovieListResponse
import com.example.movie.domain.usecase.MovieListUseCase
import com.example.movie.presentation.movie.list.viewModel.MovieListEvents
import com.example.movie.presentation.movie.list.viewModel.MoviesListViewModel
import io.mockk.*
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieListViewModelTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var loadMovieUseCase: MovieListUseCase = mockk()

    private lateinit var movieViewModel: MoviesListViewModel

    @Before
    fun setUp() {
        movieViewModel = MoviesListViewModel(loadMovieUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getPopularMovies() {
        val movieMockObj = mockk<MovieListResponse.Result>()
        every {
            movieMockObj.title
        } returns "Avenger"

        val result = MovieListEvents.OnMovieListSuccess(listOf(movieMockObj))

        coEvery {
            loadMovieUseCase.getPopularMovies()
        } returns result

        movieViewModel.getPopularMovies()

        val movieEventData = movieViewModel.movieListEvent.value

        assert(movieEventData is MovieListEvents.OnMovieListSuccess)
        assert(
            (movieEventData as MovieListEvents.OnMovieListSuccess).response?.get(0)?.title.equals(
                "Avenger",
                true
            )
        )
        assert((movieEventData).response?.count() == 1)

    }

     @Test
     fun fetchMoviesEmptyResult() {
         val result = MovieListEvents.OnMovieListSuccess(listOf())
         coEvery {
             loadMovieUseCase.getPopularMovies()
         } returns result
         movieViewModel.getPopularMovies()
         assert(movieViewModel.movieListEvent.value is MovieListEvents.OnMovieListSuccess)
         (movieViewModel.movieListEvent.value as MovieListEvents.OnMovieListSuccess).response?.isEmpty()
             ?.let { assert(it) }

     }

    @Test
    fun fetchMoviesFailure() {
        val result =
            MovieListEvents.OnMovieListFailure("Something went wrong")
        coEvery {
            loadMovieUseCase.getPopularMovies()
        } returns result

        movieViewModel.getPopularMovies()
        assert(movieViewModel.movieListEvent.value is MovieListEvents.OnMovieListFailure)
        assert((movieViewModel.movieListEvent.value as MovieListEvents.OnMovieListFailure).message == "Something went wrong")
    }

}

