package com.example.movie.presentation.movie.details.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.domain.usecase.MovieDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesDetailViewModel @Inject constructor(
    private val movieDetailsUseCase: MovieDetailsUseCase
) : ViewModel() {

    private val _movieDetailsEvent = MutableLiveData<MovieDetailEvent>(MovieDetailEvent.Void)
    val movieDetailsEvent: LiveData<MovieDetailEvent> = _movieDetailsEvent

    fun getMovieDetails(movieId: Int) {
        _movieDetailsEvent.value = MovieDetailEvent.Loading
        viewModelScope.launch(Dispatchers.Main) {

            viewModelScope.launch(Dispatchers.Main) {
                _movieDetailsEvent.postValue(
                    movieDetailsUseCase.getMovieDetails(
                        movieId = movieId
                    )
                )
            }
        }
    }

    fun updateMovieDetail(movieDetailResponse: MovieDetailResponse) {
        _movieDetailsEvent.value =
            MovieDetailEvent.OnMovieDetailSuccess(response = movieDetailResponse)
    }

}