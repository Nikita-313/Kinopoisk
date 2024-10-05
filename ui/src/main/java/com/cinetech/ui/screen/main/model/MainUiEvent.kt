package com.cinetech.ui.screen.main.model

import com.cinetech.ui.base.Reducer

sealed class MainUiEvent : Reducer.ViewEvent {
    class OnSearchTextChange(val newText: String) : MainUiEvent()
}