package com.cinetech.ui.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Main : Screen

    @Serializable
    data class Search(val searchText: String) : Screen

    @Serializable
    data class Film(val id: Long) : Screen
}