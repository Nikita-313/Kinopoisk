package com.cinetech.ui.screen.main.model

import com.cinetech.ui.base.Reducer

class MainUiReducer : Reducer<MainUiState, MainUiEvent, MainUiEffect> {
    override fun reduce(previousState: MainUiState, event: MainUiEvent): Pair<MainUiState, MainUiEffect?> {
        return when (event) {
            is MainUiEvent.OnSearchTextChange -> {
                previousState.copy(
                    searchText = event.newText,
                    isSearchFilterVisible = event.newText != ""
                ) to null
            }
        }
    }
}