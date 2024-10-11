package com.cinetech.ui.screen.film

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import com.cinetech.domain.models.Movie
import com.cinetech.ui.R
import com.cinetech.ui.composible.shimmer
import com.cinetech.ui.theme.Green
import com.cinetech.ui.theme.paddings
import com.cinetech.ui.theme.spacers
import com.cinetech.ui.utils.format
import com.cinetech.ui.utils.px
import com.cinetech.ui.utils.toPx


@Composable
fun FilmScreen(
    viewModel: FilmViewModel = hiltViewModel()
) {

    val state = viewModel.state.collectAsState()

    val topOffset = 250.dp.toPx
    val topOffsetState = remember { mutableFloatStateOf(topOffset) }

    Scaffold(
        topBar = {
            AppBar(topOffsetState.floatValue == 0f) {}
        }
    ) { paddingValues ->

        val lazyScrollState = rememberLazyListState()
        val minOffset = 0f
        val maxOffset = LocalConfiguration.current.screenHeightDp.dp.toPx

        BackgroundImage(
            image = state.value.poster,
            topOffsetState = topOffsetState
        )

        if (!state.value.isLoading)
            MainContent(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .customNestedScroll(
                        enable = !state.value.isLoading,
                        offsetY = topOffsetState,
                        minOffset = minOffset,
                        maxOffset = maxOffset,
                        lazyScrollState = lazyScrollState,
                        peekHeight = 120.dp.toPx,
                    ),
                topOffsetState = topOffsetState,
                lazyScrollState = lazyScrollState,
                movie = state.value.movie
            )

        if (state.value.isLoading)
            LoadingShimmer(
                modifier = Modifier
                    .padding(paddingValues)
                    .offset {
                        IntOffset(0, topOffset.toInt())
                    }
            )

    }
}

@Composable
private fun BackgroundImage(
    image: Drawable?,
    topOffsetState: State<Float>
) {

    val minPaddings = 30.dp.toPx
    val maxPaddings = 70.dp.toPx
    val topImgOffset = 80.dp.toPx

    val imgTopOffset = remember {
        derivedStateOf {
            topOffsetState.value / 5 + topImgOffset
        }
    }

    val imgPaddings = remember {
        derivedStateOf {
            (maxPaddings - topOffsetState.value / 20).coerceIn(minPaddings, maxPaddings)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondary),
        contentAlignment = Alignment.TopCenter
    ) {
        if (image != null)
            Image(
                bitmap = image.toBitmap().asImageBitmap(),
                modifier = Modifier
                    .fillMaxSize()
                    .blur(radius = 30.dp),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.6f))
        ) {

            if (image != null)
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = imgPaddings.value.px)
                        .offset {
                            IntOffset(0, imgTopOffset.value.toInt())
                        },
                    contentScale = ContentScale.FillWidth,
                    bitmap = image.toBitmap().asImageBitmap(),
                    contentDescription = "",
                )

            if (image == null)
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = imgPaddings.value.px)
                        .fillMaxHeight()
                        .offset {
                            IntOffset(0, imgTopOffset.value.toInt())
                        }
                        .background(MaterialTheme.colorScheme.secondaryContainer),
                )
        }
    }
}


@Composable
private fun MainContent(
    modifier: Modifier = Modifier,
    movie: Movie?,
    topOffsetState: State<Float>,
    lazyScrollState: LazyListState,
) {
    if (movie != null)

        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {

            LazyColumn(
                state = lazyScrollState,
                userScrollEnabled = false,
                modifier = Modifier
                    .offset {
                        IntOffset(0, topOffsetState.value.toInt())
                    }
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item { DragIcon() }
                item {
                    Header(
                        title = movie.name,
                        kpRating = movie.kpRating,
                        watchNumber = movie.kpVotesNumber,
                        enTitle = movie.enName,
                        releaseYear = movie.year,
                        filmCountry = movie.countries.firstOrNull() ?: "",
                        filmDurationMin = 90,
                        ageRating = movie.ageRating,
                    )
                }
                item { Description(description = movie.description) }
                item { ThrillersAndTeasers() }
                item { Rating() }
                item { Actors() }
            }

        }
}

@Composable
private fun Actors() {
    Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))
        SectionTitle(
            title = stringResource(R.string.film_screen_actors),
            onClickAllButton = {}
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))
        Column(
            modifier = Modifier.padding(start = MaterialTheme.paddings.medium)
        ) {
            ActorCard()
            Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
            ActorCard()
            Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
            ActorCard()
        }
    }
}

@Composable
private fun ActorCard() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .width(60.dp)
                .height(90.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )

        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Column {
            Text(
                text = "Харума Миура",
            )
            Text(
                text = "Eren",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun Rating() {
    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))
        SectionTitle(stringResource(R.string.film_screen_kp_rate))
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.paddings.medium)
                .background(MaterialTheme.colorScheme.secondaryContainer)
                .padding(vertical = MaterialTheme.paddings.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "4.5",
                color = MaterialTheme.colorScheme.error,
                style = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 60.sp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
            Text(
                text = "20 655 оценок",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
            Button(
                modifier = Modifier
                    .width(180.dp)
                    .height(50.dp),
                onClick = {}
            ) {
                Text("Оценить")
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
        Row(
            modifier = Modifier.padding(start = MaterialTheme.paddings.medium)
        ) {
            RatingCard()
        }

    }
}

@Composable
private fun RatingCard() {
    Row(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .padding(MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "5.0",
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
        )
        Spacer(modifier = Modifier.width(MaterialTheme.spacers.medium))
        Column {
            Text(
                text = "Рейтинг IMDb",
            )
            Text(
                text = "15 685 оценок",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ThrillersAndTeasers() {
    Column {
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))
        SectionTitle(
            title = stringResource(R.string.film_screen_trailers_adn_tisers),
            onClickAllButton = {}
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
        Row(
            modifier = Modifier.padding(start = MaterialTheme.paddings.medium)
        ) {
            ThrillerCard()
        }
    }
}

@Composable
private fun ThrillerCard() {
    Column {
        Spacer(
            modifier = Modifier
                .width(300.dp)
                .height(200.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
        Text(
            text = "Трейлер (дублированный)",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = "17 августа 2015",
            color = MaterialTheme.colorScheme.onSecondary,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SectionTitle(
    title: String,
    onClickAllButton: (() -> Unit?)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.paddings.medium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
        Spacer(modifier = Modifier.weight(1f))
        if (onClickAllButton != null)
            Text(
                text = "Все",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
            )
    }
}

@Composable
private fun Description(description: String) {
    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium),
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))
        Text(
            text = description,
            maxLines = 3
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
        Text(
            text = stringResource(R.string.film_screen_all_details),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )
    }
}

@Composable
private fun Header(
    title: String,
    kpRating: Double,
    watchNumber: Int,
    enTitle: String,
    releaseYear: Int,
    filmCountry: String,
    filmDurationMin: Int,
    ageRating: Int,
) {

    val kpRatingStr = kpRating.format(1)
    val kpRatingColor = if (kpRating < 7.0 && kpRating >= 5) MaterialTheme.colorScheme.onSecondary else if (kpRating < 5.0) MaterialTheme.colorScheme.error else Green
    val watchNumberStr = watchNumber.toString() // ToDo K-format (20412  -> 20K)

    val filmDurationMinStr = filmDurationMin.toString() // ToDo time-format (90  -> 1 час 30 мин)

    Column(
        modifier = Modifier.padding(horizontal = MaterialTheme.paddings.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))

        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.ExtraBold)
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))

        val infoTextStyle = MaterialTheme.typography.bodySmall

        Row {
            Text(
                text = kpRatingStr,
                color = kpRatingColor,
                textAlign = TextAlign.Center,
                style = infoTextStyle
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = watchNumberStr,
                color = MaterialTheme.colorScheme.secondary,
                textAlign = TextAlign.Center,
                style = infoTextStyle
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = enTitle,
                textAlign = TextAlign.Center,
                style = infoTextStyle
            )
        }
        Text(
            modifier = Modifier.width(180.dp),
            text = "$releaseYear, $filmCountry, $filmDurationMinStr, $ageRating+",
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            style = infoTextStyle
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacers.medium))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Star,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.film_screen_estimate),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.secondary,
                    style = infoTextStyle
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Favorite,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.film_screen_will_be_watching),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.secondary,
                    style = infoTextStyle
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Email,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.film_screen_share),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.secondary,
                    style = infoTextStyle
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.secondary,
                )
                Text(
                    text = stringResource(R.string.film_screen_more),
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    color = MaterialTheme.colorScheme.secondary,
                    style = infoTextStyle
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(
    isColorTransparent: Boolean,
    onNavigate: () -> Unit
) {

    val navigationIcon = @Composable {
        IconButton(onClick = onNavigate) {
            Icon(imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "")
        }
    }

    CenterAlignedTopAppBar(
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
        ),
        navigationIcon = navigationIcon
    )

    AnimatedVisibility(
        visible = isColorTransparent,
        enter = fadeIn(
            animationSpec = tween(durationMillis = 200)
        ),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 200)
        )
    ) {
        CenterAlignedTopAppBar(
            modifier = Modifier.shadow(
                elevation = 5.dp,
                spotColor = Color.DarkGray,
            ),
            title = { Text("Атака титанов") },
            navigationIcon = navigationIcon
        )
    }

}

@Composable
private fun DragIcon() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.paddings.medium),
        contentAlignment = Alignment.Center
    ) {
        Spacer(
            modifier = Modifier
                .width(40.dp)
                .height(4.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer)
        )
    }
}

@Composable
private fun LoadingShimmer(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.paddings.large),
        ) {
            DragIcon()
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Skeleton(Modifier.width(200.dp))
                Skeleton(Modifier.width(150.dp))
                Skeleton(Modifier.width(240.dp))
            }
            Spacer(Modifier.height(MaterialTheme.spacers.large))
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp)
            )
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 60.dp)
            )
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp)
            )
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 30.dp)
            )
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 60.dp)
            )
            Skeleton(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 100.dp)
            )
        }
        Spacer(
            Modifier
                .fillMaxSize()
                .shimmer()
        )
    }
}

@Composable
private fun Skeleton(modifier: Modifier) {
    Spacer(
        modifier = modifier
            .padding(bottom = MaterialTheme.paddings.small)
            .height(14.dp)
            .background(MaterialTheme.colorScheme.secondaryContainer)
    )
}