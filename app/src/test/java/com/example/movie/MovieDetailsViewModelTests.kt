package com.example.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.domain.usecase.MovieDetailsUseCase
import com.example.movie.presentation.movie.details.viewModel.MovieDetailEvent
import com.example.movie.presentation.movie.details.viewModel.MoviesDetailViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MovieDetailsViewModelTests {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private var movieDetailUseCase: MovieDetailsUseCase = mockk()

    private lateinit var movieDetailViewModel: MoviesDetailViewModel

    @Before
    fun setUp() {
        movieDetailViewModel = MoviesDetailViewModel(movieDetailUseCase)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMoviesDetails() {
        val movieMockObj = mockk<MovieDetailResponse>()
        every {
            movieMockObj.title
        } returns "TROLL"

        val result = MovieDetailEvent.OnMovieDetailSuccess(movieMockObj)

        coEvery {
            movieDetailUseCase.getMovieDetails(movieId = 1)
        } returns result

        movieDetailViewModel.getMovieDetails(movieId = 1)

        val movieEventData = movieDetailViewModel.movieDetailsEvent.value

        assert(movieEventData is MovieDetailEvent.OnMovieDetailSuccess)
        assert(
            (movieEventData as MovieDetailEvent.OnMovieDetailSuccess).response?.title.equals(
                "TROLL",
                true
            )
        )

    }

    @Test
    fun fetchMoviesFailure() {
        val result =
            MovieDetailEvent.OnMovieDetailFailure("Something went wrong")
        coEvery {
            movieDetailUseCase.getMovieDetails(movieId = 1)
        } returns result

        movieDetailViewModel.getMovieDetails(movieId = 1)
        assert(movieDetailViewModel.movieDetailsEvent.value is MovieDetailEvent.OnMovieDetailFailure)
        assert((movieDetailViewModel.movieDetailsEvent.value as MovieDetailEvent.OnMovieDetailFailure).message == "Something went wrong")
    }

}

