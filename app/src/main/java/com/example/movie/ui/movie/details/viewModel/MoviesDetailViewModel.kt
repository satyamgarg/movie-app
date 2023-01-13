package com.example.movie.ui.movie.details.viewModel

import androidx.lifecycle.viewModelScope
import com.example.movie.api.MovieRepository
import com.example.movie.ui.BaseViewModel
import com.example.movie.utils.ResultType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : BaseViewModel() {

    private val _movieListEvent = MutableStateFlow<MovieDetailEvent>(MovieDetailEvent.Void)
    var movieListEvent: StateFlow<MovieDetailEvent> = _movieListEvent

    fun getMovieDetails(apiKey: String, language: String, movieId: Int) {
        _movieListEvent.value = MovieDetailEvent.Loading
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = movieRepository.getMovieDetails(
                apiKey = apiKey,
                language = language,
                movieId = movieId
            )) {
                is ResultType.Success -> {
                    _movieListEvent.value =
                        MovieDetailEvent.OnMovieDetailSuccess(response = response.value)
                }
                is ResultType.Error -> {
                    _movieListEvent.value =
                        MovieDetailEvent.OnMovieDetailFailure(message = response.errorMessage)
                }
            }
        }
    }

}