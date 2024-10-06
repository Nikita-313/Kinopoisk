package com.cinetech.ui.screen.main

import com.cinetech.domain.models.SearchHistory
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
                ) to MainUiEffect.ScrollToTop

            }

            is MainUiEvent.MoviesLoading -> {
                previousState.copy(
                    searchInProgress = event.isLoading
                ) to null
            }

            is MainUiEvent.UpdateSearchHistoryMovie -> {
                val newSearchHistory = mutableListOf<SearchHistory>()
                newSearchHistory.addAll(previousState.searchHistoryText)
                newSearchHistory.addAll(event.searchHistory)

                previousState.copy(
                    searchHistory = newSearchHistory.sortedByDescending { it.searchTimeMs },
                    searchHistoryMovie = event.searchHistory
                ) to null
            }

            is MainUiEvent.UpdateSearchHistoryText -> {
                val newSearchHistory = mutableListOf<SearchHistory>()
                newSearchHistory.addAll(previousState.searchHistoryMovie)
                newSearchHistory.addAll(event.searchHistory)

                previousState.copy(
                    searchHistory = newSearchHistory.sortedByDescending { it.searchTimeMs },
                    searchHistoryText = event.searchHistory
                ) to null
            }
        }
    }
}