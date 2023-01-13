package com.example.movie.ui.movie.list.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movie.api.MovieRepository
import com.example.movie.data.MovieListResponse
import com.example.movie.utils.ResultType

open class MovieListPageSource(
    private val errorEvent: MutableLiveData<String>,
    private val apiKey: String,
    private val language: String,
    private var repository: MovieRepository
) :
    PagingSource<Long, MovieListResponse.Result>() {

    private val firstPageIndex = 1L
    override fun getRefreshKey(state: PagingState<Long, MovieListResponse.Result>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, MovieListResponse.Result> {
        return try {
            val nextPage = params.key ?: firstPageIndex
            when (val response =
                repository.getPopularMovies(apiKey = apiKey, language = language, page = nextPage)) {
                is ResultType.Success -> {
                    val nextKey =
                        if (response.value.results?.isEmpty() == true) {
                            null
                        } else {
                            if (response.value.totalPages == nextPage.toInt()) {
                                null
                            } else {
                                nextPage + 1
                            }
                        }
                    LoadResult.Page(
                        data = response.value.results ?: emptyList(),
                        prevKey = if (nextPage == firstPageIndex) null else nextPage - 1,
                        nextKey = nextKey
                    )
                }
                is ResultType.Error -> {
                    val message = response.errorMessage
                    errorEvent.postValue(
                        message
                    )
                    LoadResult.Error(Exception(message))
                }
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}