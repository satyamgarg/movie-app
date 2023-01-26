package com.example.movie.presentation.movie.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.R
import com.example.movie.data.response.MovieListResponse
import com.example.movie.presentation.movie.list.viewModel.MovieIntent
import com.example.movie.presentation.movie.list.viewModel.MovieListEvents
import com.example.movie.presentation.movie.list.viewModel.MoviesListViewModel
import com.example.movie.utils.Constants
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieListScreen(viewModel: MoviesListViewModel = hiltViewModel(), OnMovieClick :(MovieListResponse.Result) -> Unit) {

    LaunchedEffect(key1 = Unit, block = {
        viewModel.channel.send(MovieIntent.GetMovies)
    })

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colorResource(id = R.color.black),
                contentColor = Color.White,
                elevation = 8.dp,
                title = {
                    Text(text = stringResource(id = R.string.movie_list))
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {

            when (val state = viewModel.movieListEvent.collectAsState().value) {
                is MovieListEvents.OnMovieListSuccess -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(start = 20.dp, end = 20.dp)

                    ) {
                        items(state.response?.size ?: 0) { index ->

                            state.response?.get(index)?.let { movie ->

                                MovieListItem(movie = movie, onMovieClick = {
                                    OnMovieClick(movie)
                                })
                            }
                        }
                    }
                }
                else -> Unit
            }
        }
    }
}

@Composable
private fun MovieListItem(
    movie: MovieListResponse.Result,
    horizontal: Dp = 10.dp,
    vertical: Dp = 5.dp,
    onMovieClick: (() -> Unit)? = null
) {
    GlideImage(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontal, vertical = vertical)
            .height(400.dp)
            .clip(RoundedCornerShape(size = 5.dp))
            .clickable { onMovieClick?.invoke() },
        imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
        imageModel = {
            "${Constants.IMAGE_URL}${movie.posterPath}"
        },
        loading = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Gray)
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        },
        failure = {
            Image(
                painter = painterResource(id = R.drawable.ic_downloading),
                contentDescription = stringResource(
                    id = R.string.failed_to_load_image
                )
            )
        }
    )
}


