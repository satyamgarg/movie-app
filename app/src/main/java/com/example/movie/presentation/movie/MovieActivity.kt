package com.example.movie.presentation.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.example.movie.navigation.AppNav
import com.example.movie.navigation.AppNavHost
import com.example.movie.presentation.theme.MovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieActivity : ComponentActivity() {
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            navController = rememberNavController()
            MovieTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AppNavHost(modifier = Modifier, navController = navController)
                }
            }
        }
    }

    fun navigateTo(
        destination: AppNav,
        vararg args: Pair<String, Any?>,
        popToStore: Boolean = false,
        isInclusive: Boolean = false,
    ) {
        val arguments = args.joinToString(separator = "&") {
            "${it.first}=${it.second}"
        }

        if (this::navController.isInitialized) {
            navController.navigate(
                route = destination.route + if (args.isEmpty()) "" else "?$arguments",
                navOptions = if (popToStore) {
                    NavOptions.Builder().apply {
                        setPopUpTo(
                            destinationId = navController.graph.startDestinationId,
                            inclusive = isInclusive
                        )
                    }.build()
                } else null
            )
        }
    }

}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MovieTheme {
        Greeting("Android")
    }
}