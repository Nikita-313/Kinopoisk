package com.cinetech.ui.screen.main.model

import com.cinetech.ui.base.Reducer

data class MainUiState(
    val searchText: String = "",
    val isSearchFilterVisible:Boolean = false,
) : Reducer.ViewState