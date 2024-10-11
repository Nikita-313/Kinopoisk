package com.cinetech.ui.screen.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.ui.R
import com.cinetech.ui.navigation.Screen
import com.cinetech.ui.theme.Green
import com.cinetech.ui.theme.paddings
import com.cinetech.ui.theme.spacers
import com.cinetech.ui.utils.format

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = hiltViewModel(),
    onPop: () -> Unit,
    onNavigate: (Screen) -> Unit
) {

    val movieItems = viewModel.searchMovies.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            AppBar(
                searchText = viewModel.searchText,
                onNavigate = onPop
            )
        }
    ) { paddingValues ->


        if (movieItems.loadState.refresh is LoadState.Loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        if (movieItems.loadState.refresh is LoadState.Error) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = MaterialTheme.paddings.medium),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = Modifier.width(200.dp),
                    text = stringResource(R.string.search_screen_connection_error),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.search_screen_check_internet),
                    color = MaterialTheme.colorScheme.onSecondary,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { movieItems.refresh() }) {
                    Text(
                        text = stringResource(R.string.search_screen_refresh),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(paddingValues),
        ) {

            if (movieItems.itemCount != 0)
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.paddings.medium),
                        text = stringResource(R.string.search_screen_found_films),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }

            items(
                count = movieItems.itemCount,
                key = movieItems.itemKey { it.id }
            ) { index ->
                val movie = movieItems[index]
                if (movie != null) {
                    MovieCard(
                        movie = movie,
                        onCardClick = {
                            onNavigate(Screen.Film(it.id))
                        }
                    )
                }
            }

            movieItems.apply {

                if (loadState.append is LoadState.Loading) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.paddings.medium),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                if (loadState.append is LoadState.Error) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = MaterialTheme.paddings.medium),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.search_screen_connection_error),
                                color = MaterialTheme.colorScheme.onSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            TextButton(onClick = { movieItems.retry() }) {
                                Text(
                                    text = stringResource(R.string.search_screen_refresh),
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MovieCard(
    movie: PreviewMovie,
    onCardClick: (PreviewMovie) -> Unit
) {
    val name = movie.name
    val enName = if (movie.alternativeName == "") movie.year.toString() else "${movie.alternativeName}, " + movie.year
    val kpRating = movie.kpRating.format(1)
    val kpRatingColor = if (movie.kpRating < 7.0 && movie.kpRating >= 5) MaterialTheme.colorScheme.onSecondary else if (movie.kpRating < 5.0) MaterialTheme.colorScheme.error else Green
    val countries = if (movie.countries.isNotEmpty()) movie.countries.reduce { result, nr -> "$result; $nr" } else ""

    Column(
        modifier = Modifier
            .clickable { onCardClick(movie) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.paddings.small, bottom = MaterialTheme.paddings.small, start = MaterialTheme.paddings.small),
            verticalAlignment = Alignment.Top
        ) {

            AsyncImage(
                modifier = Modifier
                    .width(80.dp)
                    .height(90.dp),
                model = movie.previewUrl,
                contentDescription = "",
                placeholder = painterResource(R.drawable.ic_logo_foreground),
                error = painterResource(R.drawable.ic_logo_foreground),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacers.small))
            Column(
                modifier = Modifier
                    .height(95.dp)
                    .weight(1f),
            ) {
                Text(
                    text = name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = enName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    text = countries,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.main_screen_search_film_card_watch),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                )
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
            Column(modifier = Modifier.fillMaxHeight()) {
                Text(
                    text = kpRating,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = kpRatingColor
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
        HorizontalDivider(
            modifier = Modifier.padding(start = 91.dp),
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    searchText: String,
    onNavigate: () -> Unit,
) {

    CenterAlignedTopAppBar(
        modifier = Modifier.shadow(
            elevation = 5.dp,
            spotColor = Color.DarkGray,
        ),
        title = {
            Text(
                text = "${stringResource(R.string.search_screen_title)}: $searchText",
                style = MaterialTheme.typography.titleMedium,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigate) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "")
            }
        },
    )
}