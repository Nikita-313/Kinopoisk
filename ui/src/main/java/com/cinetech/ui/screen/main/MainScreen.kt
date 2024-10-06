package com.cinetech.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Clear
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cinetech.domain.models.SearchMovie
import com.cinetech.ui.R
import com.cinetech.ui.theme.Green
import com.cinetech.ui.theme.Orange
import com.cinetech.ui.theme.paddings
import com.cinetech.ui.theme.spacers
import com.cinetech.ui.utils.format

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect {

        }
    }

    Scaffold(
        topBar = {
            AppBar(
                searchText = state.searchText,
                isLoading = state.searchInProgress,
                onValueChange = { viewModel.onSearchTextChange(it) },
                onClear = { viewModel.onSearchTextChange("") },
                onNavigate = {}
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
        ) {
            AnimatedVisibility(
                state.isSearchFilterVisible,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                SearchFilter()
            }

            state.movies?.docs?.forEach {
                FilmCard(it)
            }

            if (state.movies?.docs?.isNotEmpty() == true)
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(MaterialTheme.paddings.medium),
                    onClick = {},
                ) {
                    Text(stringResource(R.string.main_screen_search_watch_results))
                }

        }
    }
}

@Composable
private fun FilmCard(
    movie: SearchMovie,
) {
    val name = movie.name
    val enName = movie.enName?.let { if(it == "") "" else "$it, " } + movie.year
    val kpRating = movie.kpRating?.format(1) ?: "0.0"


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = MaterialTheme.paddings.small, horizontal = MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .width(50.dp)
                .height(65.dp)
                .background(color = Color.Black),
            imageVector = Icons.Outlined.Clear,
            contentDescription = ""
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
            color = Green
        )
    }
}

@Composable
private fun SearchFilter() {

    var select by remember { mutableStateOf(true) }
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier
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
                suffix = if (isLoading) {{
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                }} else null,
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
                )
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigate) {
                Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "")
            }
        },
        actions = {
            IconButton(onClick = onClear) {
                Icon(imageVector = Icons.Outlined.Clear, contentDescription = "")
            }
        }
    )
}