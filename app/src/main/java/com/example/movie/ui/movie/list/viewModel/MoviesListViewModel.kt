package com.example.movie.ui.movie.list.viewModel

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movie.api.MovieRepository
import com.example.movie.data.MovieListResponse
import com.example.movie.ui.BaseViewModel
import com.example.movie.ui.movie.list.paging.MovieListPageSource
import com.example.movie.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


@HiltViewModel
class MoviesListViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel() {

    val movieList: Flow<PagingData<MovieListResponse.Result>> =
        Pager(config = PagingConfig(pageSize = 1)) {
            MovieListPageSource(
                repository = repository,
                errorEvent = _errorState,
                apiKey = Constants.API_KEY,
                language = Constants.LANGUAGE
            )
        }.flow
}