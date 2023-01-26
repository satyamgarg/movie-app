package com.example.movie.presentation.movie.details

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.movie.R
import com.example.movie.data.response.MovieDetailResponse
import com.example.movie.data.response.MovieListResponse
import com.example.movie.presentation.movie.details.viewModel.MovieDetailEvent
import com.example.movie.presentation.movie.details.viewModel.MoviesDetailViewModel
import com.example.movie.presentation.movie.list.viewModel.MovieIntent
import com.example.movie.utils.Constants
import com.example.movie.utils.DateUtils
import com.google.gson.Gson
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun MovieDetailScreen(movieDetails: String, onBackPressed: () -> Unit) {
    val moviesDetailViewModel: MoviesDetailViewModel = hiltViewModel()

    val movie = Gson().fromJson(movieDetails, MovieListResponse.Result::class.java)
    val context = LocalContext.current

    movie.id?.let { movieId ->
        LaunchedEffect(key1 = Unit, block = {
            moviesDetailViewModel.channel.send(MovieIntent.GetMovieDetails(movieId = movieId))
        })
    } ?: run { onBackPressed() }

    LaunchedEffect(key1 = Unit, block = {
        moviesDetailViewModel.updateMovieDetail(
            MovieDetailResponse(
                posterPath = movie.posterPath,
                overview = movie.overview,
                releaseDate = movie.releaseDate,
                id = movie.id
            )
        )
    })

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = colorResource(id = R.color.black),
                contentColor = Color.White,
                elevation = 8.dp,
                title = {
                    movie.title?.ifEmpty { stringResource(id = R.string.app_name) }
                        ?.let { Text(text = it, maxLines = 1, overflow = TextOverflow.Ellipsis) }
                },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.app_name)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when (val response = moviesDetailViewModel.movieDetailsEvent.collectAsState().value) {
            is MovieDetailEvent.OnMovieDetailSuccess -> {

                DisplayMovieDetails(paddingValues = paddingValues, movie = response.response)
            }
            is MovieDetailEvent.OnMovieDetailFailure -> {
                Toast.makeText(context, response.message, Toast.LENGTH_LONG).show()
            }
            else -> Unit
        }
    }
}

@Composable
fun DisplayMovieDetails(paddingValues: PaddingValues, movie: MovieDetailResponse) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(state = scrollState)
    ) {
        MovieItem(
            imageUrl = movie.backdropPath.orEmpty(),
            height = 300.dp
        )
        if (movie.originalTitle?.isNotEmpty() == true) {
            Text(
                text = movie.originalTitle,
                modifier = Modifier.padding(10.dp),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (movie.genres?.isNotEmpty() == true) {
            Text(
                text = stringResource(id = R.string.genre),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp),
                color = Color.Yellow,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .padding(start = 10.dp)
        ) {
            Text(
                text = movie.genres?.map { it?.name }.toString(),
                modifier = Modifier.padding(start = 1.dp),
                color = colorResource(id = R.color.off_white),
                fontSize = 12.sp
            )
        }

        Row {
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

            if (movie.voteAverage != null) {
                Text(
                    text = "${stringResource(id = R.string.rating)}\n${movie.voteAverage}",
                    modifier = Modifier
                        .padding(10.dp)
                        .align(Alignment.CenterVertically),
                    color = Color.White
                )
            }

            if (movie.voteCount != null) {
                Text(
                    text = "${stringResource(id = R.string.vote)}\n${movie.voteCount}",
                    modifier = Modifier.padding(10.dp),
                    color = colorResource(id = R.color.off_white)
                )
            }
        }

        if (movie.productionCompanies?.isNotEmpty() == true) {
            Text(
                text = stringResource(id = R.string.production_company),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp),
                color = Color.Yellow,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        movie.productionCompanies?.forEach { company ->
            Text(
                text = "${stringResource(id = R.string.hash_tag)} ${company?.name}",
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = Color.White
            )
        }

        if (movie.productionCountries?.isNotEmpty() == true) {
            Text(
                text = stringResource(id = R.string.production_country),
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp),
                color = Color.Yellow,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
            )
        }
        movie.productionCountries?.forEach { company ->
            Text(
                text = "${stringResource(id = R.string.hash_tag)} ${company?.name}",
                modifier = Modifier.padding(start = 10.dp, end = 10.dp),
                color = colorResource(id = R.color.off_white)
            )
        }

        Text(
            text = stringResource(id = R.string.overview),
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 10.dp),
            color = Color.Yellow,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = movie.overview.orEmpty(),
            modifier = Modifier.padding(start = 10.dp, bottom = 20.dp, end = 10.dp),
            color = colorResource(id = R.color.off_white)
        )
    }
}

@Composable
fun MovieItem(
    imageUrl: String,
    height: Dp,
    horizontal: Dp = 10.dp,
    vertical: Dp = 5.dp,
    onMovieClick: (() -> Unit)? = null
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth(1f)
                .padding(horizontal = horizontal, vertical = vertical)
                .height(height)
                .clip(RoundedCornerShape(size = 5.dp))
                .clickable {
                    onMovieClick?.invoke()
                },
            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds),
            imageModel = {
                "${Constants.IMAGE_URL}${imageUrl}"
            },
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Gray)
                        .height(height),
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

}


