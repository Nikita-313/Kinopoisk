package com.cinetech.ui.screen.main.model


import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchHistory
import com.cinetech.ui.base.Reducer

data class MainUiState(
    val movies: SearchMoviePageable? = null,
    val searchHistory: List<SearchHistory> = emptyList(),
    val searchHistoryText: List<SearchHistory.Text> = emptyList(),
    val searchHistoryMovie: List<SearchHistory.Movie> = emptyList(),
    val searchText: String = "",
    val isSearchFilterVisible: Boolean = false,
    val searchInProgress: Boolean = false,
) : Reducer.ViewState