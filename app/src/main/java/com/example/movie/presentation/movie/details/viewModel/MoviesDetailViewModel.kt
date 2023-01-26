package com.example.movie.presentation.movie.details.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.domain.usecase.MovieDetailsUseCase
import com.example.movie.presentation.movie.list.viewModel.MovieIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailViewModel @Inject constructor(
    private val movieDetailsUseCase: MovieDetailsUseCase
) : ViewModel() {

    private val _movieDetailsEvent = MutableStateFlow<MovieDetailEvent>(MovieDetailEvent.Void)
    val movieDetailsEvent: StateFlow<MovieDetailEvent> = _movieDetailsEvent
    val channel = Channel<MovieIntent>()

    init {
        handleChannelEvent()
    }

    private fun handleChannelEvent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { movieIntent ->
                when (movieIntent) {
                    is MovieIntent.GetMovieDetails -> {
                        getMovieDetails(movieId = movieIntent.movieId)
                    }
                    else -> Unit
                }
            }
        }
    }

    fun getMovieDetails(movieId: Int) {
        _movieDetailsEvent.value = MovieDetailEvent.Loading
        viewModelScope.launch(Dispatchers.Main) {

            viewModelScope.launch(Dispatchers.Main) {
                _movieDetailsEvent.value =  movieDetailsUseCase.getMovieDetails(
                    movieId = movieId
                )
            }
        }
    }

    fun updateMovieDetail(movieDetailResponse: MovieDetailResponse) {
        _movieDetailsEvent.value =
            MovieDetailEvent.OnMovieDetailSuccess(response = movieDetailResponse)
    }

}