package com.cinetech.ui.screen.film

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@Composable
fun Modifier.customNestedScroll(
    offsetY: MutableState<Float>,
    minOffset: Float,
    maxOffset: Float,
    peekHeight: Float,
    lazyScrollState: LazyListState,
    enable:Boolean = true,
): Modifier {

    val initOffset = remember { offsetY.value }
    val scope = rememberCoroutineScope()
    val stopFling = remember { mutableStateOf(false) }
    val scrollState = rememberScrollableState { delta ->
        if (stopFling.value) {
            return@rememberScrollableState 0f
        }

        val lazyOffset = -lazyScrollState.dispatchRawDelta(-delta)

        if (delta < 0 && offsetY.value == minOffset) {
            return@rememberScrollableState lazyOffset
        }

        if (delta > 0 && offsetY.value == maxOffset) {
            return@rememberScrollableState 0f
        }

        delta
    }
    val connection = remember {
        SearchScreenNestedScrollConnection(
            stopFling = stopFling,
            offsetY = offsetY,
            maxOffset = maxOffset,
            minOffset = minOffset,
            lazyScrollState = lazyScrollState,
            scope = scope,
            initOffset = initOffset,
            peekHeight = peekHeight,
        )
    }

    return this then Modifier
        .nestedScroll(connection)
        .scrollable(
            enabled = enable,
            orientation = Orientation.Vertical,
            state = scrollState
        )
}

class SearchScreenNestedScrollConnection(
    private val stopFling: MutableState<Boolean>,
    private val offsetY: MutableState<Float>,
    private val minOffset: Float,
    private val maxOffset: Float,
    private val lazyScrollState: ScrollableState,
    private val scope: CoroutineScope,
    private val initOffset: Float,
    private val peekHeight: Float,
) : NestedScrollConnection {

    private var isNeedStopFlingOnCollapsedState = true
    private var animationJob: Job? = null

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        animationJob?.cancel()

        val delta = available.y

        if (delta > 0 && lazyScrollState.canScrollBackward) {
            return Offset.Zero
        }

        val newOffset = offsetY.value + delta

        if (source == NestedScrollSource.SideEffect) {
            if (
                delta > 0 &&
                offsetY.value !in (initOffset + SCROLL_BOUNDS_OFFSET..maxOffset) &&
                newOffset in (initOffset + SCROLL_BOUNDS_OFFSET..maxOffset) &&
                isNeedStopFlingOnCollapsedState
            ) {
                offsetY.value = initOffset + SCROLL_BOUNDS_OFFSET
                stopFling.value = true
                return Offset.Zero
            }

            if (delta < 0 && offsetY.value > initOffset && newOffset < initOffset) {
                offsetY.value = initOffset
                stopFling.value = true
                return Offset.Zero
            }
        }

        stopFling.value = false
        val prevSize = offsetY.value
        offsetY.value = newOffset.coerceIn(minOffset, maxOffset)
        val consumed = offsetY.value - prevSize

        return Offset(0f, consumed)
    }

    override suspend fun onPreFling(available: Velocity): Velocity {

        if (offsetY.value >= initOffset) {
            isNeedStopFlingOnCollapsedState = false
        } else {
            isNeedStopFlingOnCollapsedState = true
        }

        return super.onPreFling(available)
    }

    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {

        val stateDivider = initOffset + (maxOffset - peekHeight - initOffset) / 2

        if (offsetY.value in initOffset..stateDivider) {
            animationJob = animateTo(initOffset)
        }

        if (offsetY.value in stateDivider..maxOffset) {
            animationJob = animateTo(maxOffset - peekHeight)
        }

        return super.onPostFling(consumed, available)
    }

    private fun animateTo(
        value: Float,
    ): Job {
        return scope.launch {
            animate(
                initialValue = offsetY.value,
                targetValue = value,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) { value, velocity ->
                offsetY.value = value
            }

        }
    }

    companion object {
        const val SCROLL_BOUNDS_OFFSET = 400f
    }

}