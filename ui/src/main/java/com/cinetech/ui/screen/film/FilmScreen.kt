package com.cinetech.ui.screen.film

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.cinetech.ui.screen.search.SearchScreenNestedScrollConnection
import com.cinetech.ui.theme.paddings
import com.cinetech.ui.utils.toPx


@Composable
fun FilmScreen() {

    val sizeState = remember { mutableStateOf(900f) }

    Scaffold(
        topBar = {

            AppBar(sizeState.value == 0f) {}
        }
    ) { paddingValues ->
        val lazyScrollState = rememberLazyListState()

        val minOffset = 0f
        val maxOffset = LocalConfiguration.current.screenHeightDp.dp.toPx

        val scope = rememberCoroutineScope()

        val connection = remember {

            SearchScreenNestedScrollConnection(
                sizeState = sizeState,
                maxOffset = maxOffset,
                minOffset = minOffset,
                lazyScrollState = lazyScrollState,
                scope = scope,
            )
        }


        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(color = Color.Red)
                .nestedScroll(connection)
                .scrollable(
                    orientation = Orientation.Vertical,
                    state = rememberScrollableState { delta ->

                        val lazyOffset = -lazyScrollState.dispatchRawDelta(-delta)

                        if (delta < 0 && sizeState.value == minOffset) {
                            return@rememberScrollableState lazyOffset
                        }

                        if (delta > 0 && sizeState.value == maxOffset) {
                            return@rememberScrollableState 0f
                        }

                        delta
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {

            Image(
                modifier = Modifier.size(300.dp),
                imageVector = Icons.Default.AccountCircle,
                contentDescription = ""
            )

            LazyColumn(
                state = lazyScrollState,
                userScrollEnabled = false,
                modifier = Modifier
                    .offset { IntOffset(0, sizeState.value.toInt()) }
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.paddings.small),
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

                items(100) {
                    Text("hello $it")
                }
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

    AnimatedVisibility(
        visible = !isColorTransparent,
        enter = fadeIn(),
        exit = fadeOut(
            animationSpec = tween(durationMillis = 0)
        )
    ) {
        CenterAlignedTopAppBar(
            title = {},
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
            ),
            navigationIcon = navigationIcon
        )
    }

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
