package com.example.movie.ui.movie.list

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.example.movie.R
import com.example.movie.data.MovieListResponse
import com.example.movie.navigation.AppNav
import com.example.movie.ui.movie.MovieActivity
import com.example.movie.ui.movie.list.viewModel.MoviesListViewModel
import com.example.movie.utils.Constants
import com.example.movie.utils.DateUtils
import com.google.gson.Gson
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieListScreen(viewModel: MoviesListViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val movieList = viewModel.movieList.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current

    viewModel.errorState.observe(lifecycleOwner) { message ->
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                content = {
                    items(
                        items = movieList
                    ) { item ->
                        item?.let { movie ->
                            MovieListItem(
                                movie = movie,
                                onMovieClick = {
                                    AppNav.MovieDetail.navigate(
                                        movieActivity = context as MovieActivity,
                                        movie = Gson().toJson(movie)
                                    )
                                })
                        }
                    }
                })
        }
    }
}

@Composable
fun MovieListItem(
    movie: MovieListResponse.Result,
    horizontal: Dp = 10.dp,
    vertical: Dp = 5.dp,
    onMovieClick: (() -> Unit)? = null
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable {
            onMovieClick?.invoke()
        }) {
        GlideImage(
            modifier = Modifier
                .width(180.dp)
                .padding(horizontal = horizontal, vertical = vertical)
                .height(240.dp)
                .clip(RoundedCornerShape(size = 5.dp)),
            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
            imageModel = {
                "${Constants.IMAGE_URL}${movie.posterPath}"
            },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Gray)
                        .height(240.dp),
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
        Column(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            if (movie.title?.isNotEmpty() == true) {
                Text(
                    text = movie.title,
                    modifier = Modifier.padding(10.dp),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(modifier = Modifier.fillMaxWidth()) {
                if (movie.releaseDate?.isNotEmpty() == true) {
                    Text(
                        text = "${stringResource(id = R.string.release_date)}\n${
                            DateUtils.convertDateFormat(
                                movie.releaseDate,
                                Constants.SERVER_DATE_FORMAT,
                                Constants.DATE_FORMAT
                            )
                        }",
                        modifier = Modifier.padding(10.dp),
                        color = Color.White
                    )
                }
            }
            if (movie.voteAverage != null) {
                Text(
                    text = "${stringResource(id = R.string.rating)} ${movie.voteAverage}",
                    modifier = Modifier.padding(10.dp),
                    color = Color.White
                )
            }
        }
    }
}


