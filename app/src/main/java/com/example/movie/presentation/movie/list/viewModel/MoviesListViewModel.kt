package com.example.movie.presentation.movie.list.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.domain.usecase.MovieListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val movieListUseCase: MovieListUseCase
) : ViewModel() {

    private val _moviesEvent = MutableLiveData<MovieListEvents>(MovieListEvents.Loading)
    val movieListEvent: LiveData<MovieListEvents> get() = _moviesEvent

    init {
        getPopularMovies()
    }

    fun getPopularMovies() {
        _moviesEvent.value = MovieListEvents.Loading
        viewModelScope.launch(Dispatchers.Main) {
            _moviesEvent.postValue(movieListUseCase.getPopularMovies())
        }
    }

}