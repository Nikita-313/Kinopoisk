package com.cinetech.ui.screen.main

import com.cinetech.ui.base.Reducer
import com.cinetech.ui.screen.main.model.MainUiEffect
import com.cinetech.ui.screen.main.model.MainUiEvent
import com.cinetech.ui.screen.main.model.MainUiState

class MainUiReducer : Reducer<MainUiState, MainUiEvent, MainUiEffect> {
    override fun reduce(previousState: MainUiState, event: MainUiEvent): Pair<MainUiState, MainUiEffect?> {
        return when (event) {
            is MainUiEvent.OnSearchTextChange -> {
                previousState.copy(
                    searchText = event.newText,
                    isSearchFilterVisible = event.newText != ""
                ) to null
            }

            is MainUiEvent.UpdateMovies -> {
                previousState.copy(
                    movies = event.movies
                ) to null

            }

            is MainUiEvent.MoviesLoading -> {
                previousState.copy(
                    searchInProgress = event.isLoading
                ) to null
            }
        }
    }
}