package com.cinetech.ui.screen.main.model


import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.ui.base.Reducer

data class MainUiState(
    val movies: SearchMoviePageable? = null,
    val searchText: String = "",
    val isSearchFilterVisible: Boolean = false,
    val searchInProgress: Boolean = false,
) : Reducer.ViewState