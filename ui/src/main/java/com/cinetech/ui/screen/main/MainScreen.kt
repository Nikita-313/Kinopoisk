package com.cinetech.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.SearchHistory
import com.cinetech.ui.R
import com.cinetech.ui.theme.Green
import com.cinetech.ui.theme.Grey
import com.cinetech.ui.theme.Orange
import com.cinetech.ui.theme.paddings
import com.cinetech.ui.theme.spacers
import com.cinetech.ui.utils.format

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val searchMovies = state.movies?.docs ?: emptyList()
    val searchHistory = state.searchHistory

    val scrollSate = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {
            scrollSate.animateScrollToItem(0)
        }
    }

    LaunchedEffect(state.isSearchFilterVisible) {
        if(state.isSearchFilterVisible)
        scrollSate.animateScrollToItem(0)
    }

    LaunchedEffect(searchMovies.isEmpty()) {
        if(searchMovies.isEmpty())
            scrollSate.animateScrollToItem(0)
    }

    Scaffold(
        topBar = {
            AppBar(
                searchText = state.searchText,
                isLoading = state.searchInProgress,
                onValueChange = { viewModel.onSearchTextChange(it) },
                onClear = { viewModel.onSearchTextChange("") },
                onSearch = { viewModel.saveTextHistory(it) },
                onNavigate = {}
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .imePadding()
                .fillMaxSize(),
            state = scrollSate,
        ) {

            item {
                AnimatedVisibility(
                    state.isSearchFilterVisible,
                    enter = expandVertically(),
                    exit = shrinkVertically()
                ) {
                    SearchFilter()
                }
            }

            if (searchMovies.isEmpty()) {
                items(
                    count = searchHistory.size,
                    key = {
                        when (val element = searchHistory[it]) {
                            is SearchHistory.Movie -> element.previewMovie.id
                            is SearchHistory.Text -> element.text
                        }
                    },
                ) {
                    when (val element = searchHistory[it]) {
                        is SearchHistory.Movie -> {
                            FilmHistoryCard(
                                modifier = Modifier.animateItem(),
                                movie = element.previewMovie,
                                onCardClick = viewModel::saveMovieHistory,
                                onDeleteClick = viewModel::deleteMovieHistory
                            )
                        }

                        is SearchHistory.Text -> {
                            SearchTextHistoryCard(
                                modifier = Modifier.animateItem(),
                                text = element.text,
                                onCardClick = viewModel::saveTextHistory,
                                onDeleteClick = viewModel::deleteTextHistory
                            )
                        }
                    }
                }
            }

            items(
                count = searchMovies.size,
                key = {searchMovies[it].id}
            ) {
                FilmCard(
                    movie = searchMovies[it],
                    onCardClick = viewModel::saveMovieHistory
                )
            }

            if (searchMovies.isNotEmpty())
                item {
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(MaterialTheme.paddings.medium),
                        onClick = { viewModel.saveTextHistory(state.searchText) },
                    ) {
                        Text(stringResource(R.string.main_screen_search_watch_results))
                    }
                }

        }
    }
}

@Composable
private fun SearchTextHistoryCard(
    modifier: Modifier = Modifier,
    text: String,
    onCardClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .clickable { onCardClick(text) }
            .padding(vertical = MaterialTheme.paddings.small, horizontal = MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(50.dp)) {
            Icon(
                modifier = Modifier
                    .clip(CircleShape)
                    .size(40.dp)
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                    .padding(10.dp),
                imageVector = Icons.Outlined.Search, contentDescription = "",
                tint = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Text(
            modifier = Modifier.weight(1f),
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
        IconButton(onClick = { (onDeleteClick(text)) }) {
            Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
        }
    }
}

@Composable
private fun FilmHistoryCard(
    modifier: Modifier = Modifier,
    movie: PreviewMovie,
    onCardClick: (PreviewMovie) -> Unit,
    onDeleteClick: (Long) -> Unit
) {
    val name = movie.name
    val enName = if (movie.alternativeName == "") movie.year.toString() else "${movie.alternativeName}, " + movie.year

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onCardClick(movie) }
            .padding(vertical = MaterialTheme.paddings.small, horizontal = MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        AsyncImage(
            modifier = Modifier
                .width(50.dp)
                .height(65.dp),
            model = movie.previewUrl,
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_logo_foreground),
            error = painterResource(R.drawable.ic_logo_foreground),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Column(
            modifier = Modifier.weight(1f),
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        IconButton(onClick = { onDeleteClick(movie.id) }) {
            Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
        }
    }
}

@Composable
private fun FilmCard(
    movie: PreviewMovie,
    onCardClick: (PreviewMovie) -> Unit
) {
    val name = movie.name
    val enName = if (movie.alternativeName == "") movie.year.toString() else "${movie.alternativeName}, " + movie.year
    val kpRating = movie.kpRating.format(1)
    val kpRatingColor = if (movie.kpRating < 7.0) Grey else Green

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick(movie) }
            .padding(vertical = MaterialTheme.paddings.small, horizontal = MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        AsyncImage(
            modifier = Modifier
                .width(50.dp)
                .height(65.dp),
            model = movie.previewUrl,
            contentDescription = "",
            placeholder = painterResource(R.drawable.ic_logo_foreground),
            error = painterResource(R.drawable.ic_logo_foreground),
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Column(
            modifier = Modifier.weight(1f),
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
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
            Text(
                text = stringResource(R.string.main_screen_search_film_card_watch),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Orange,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            )
        }
        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Text(
            text = kpRating,
            style = MaterialTheme.typography.titleMedium,
            color = kpRatingColor
        )
    }
}

@Composable
private fun SearchFilter(
    modifier: Modifier = Modifier
) {

    var select by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .height(65.dp)
            .padding(MaterialTheme.paddings.medium)
            .shadow(
                elevation = 5.dp,
                spotColor = Color.DarkGray,
                shape = RoundedCornerShape(10.dp)
            ),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        select = true
                    },
                color = if (select) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.main_screen_search_filter_all_results),
                        color = if (select) MaterialTheme.colorScheme.background else Color.Unspecified,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) {
                        select = false
                    },
                color = if (!select) MaterialTheme.colorScheme.onBackground else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.main_screen_search_filter_online_cinema),
                        color = if (!select) MaterialTheme.colorScheme.background else Color.Unspecified,
                        style = MaterialTheme.typography.titleSmall,
                    )
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    searchText: String,
    isLoading: Boolean,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    onNavigate: () -> Unit,
    onSearch: (String) -> Unit,
) {

    TopAppBar(
        modifier = Modifier.shadow(
            elevation = 5.dp,
            spotColor = Color.DarkGray,
        ),
        title = {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchText,
                onValueChange = onValueChange,
                textStyle = MaterialTheme.typography.titleSmall,
                suffix = if (isLoading) {
                    {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    }
                } else null,
                placeholder = {
                    Text(
                        stringResource(R.string.main_screen_search_title),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    errorBorderColor = Color.Transparent,
                    disabledBorderColor = Color.Transparent,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                ),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { onSearch(searchText) }
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigate) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "")
            }
        },
        actions = {
            if (searchText != "")
                IconButton(onClick = onClear) {
                    Icon(imageVector = Icons.Outlined.Clear, contentDescription = "", tint = MaterialTheme.colorScheme.onPrimary)
                }
        }
    )
}