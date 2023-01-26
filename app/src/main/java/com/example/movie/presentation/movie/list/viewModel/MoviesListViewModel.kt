package com.example.movie.presentation.movie.list.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

    val channel = Channel<MovieIntent>()
    private val _moviesEvent = MutableStateFlow<MovieListEvents>(MovieListEvents.Loading)
    val movieListEvent: StateFlow<MovieListEvents> get() = _moviesEvent

    init {
        handleChannelEvent()
    }

    private fun handleChannelEvent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect { movieIntent ->
                when (movieIntent) {
                    is MovieIntent.GetMovies -> {
                        getPopularMovies()
                    }
                    else -> Unit
                }
            }
        }
    }

    fun getPopularMovies() {
        _moviesEvent.value = MovieListEvents.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _moviesEvent.value = movieListUseCase.getPopularMovies()
        }
    }

}