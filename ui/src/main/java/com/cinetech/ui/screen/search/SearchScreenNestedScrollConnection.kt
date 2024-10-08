package com.cinetech.ui.screen.search

import androidx.compose.animation.core.animate
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SearchScreenNestedScrollConnection(
    private val sizeState: MutableState<Float>,
    private val minOffset: Float,
    private val maxOffset: Float,
    private val lazyScrollState: LazyListState,
    private val scope:CoroutineScope,
) : NestedScrollConnection {

    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.y

        if (delta > 0 && lazyScrollState.firstVisibleItemIndex > 0) {
            return Offset.Zero
        }

        if (lazyScrollState.firstVisibleItemIndex == 0 && lazyScrollState.firstVisibleItemScrollOffset > 0) {
            return Offset.Zero
        }

        val newSize = sizeState.value + delta
        val prevSize = sizeState.value
        sizeState.value = newSize.coerceIn(minOffset, maxOffset)
        val consumed = sizeState.value - prevSize

        return Offset(0f, consumed)
    }


    override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {


        if (sizeState.value in maxOffset - 300f..maxOffset) {
            animateTo(maxOffset - 300f)
        } else if (sizeState.value in 900f..maxOffset) {
            animateTo(900f)
        }

        return super.onPostFling(consumed, available)
    }

    private fun animateTo(value: Float) {
        scope.launch {
            animate(
                initialValue = sizeState.value,
                targetValue = value
            ) { value, velocity ->
                sizeState.value = value
            }
        }
    }

}