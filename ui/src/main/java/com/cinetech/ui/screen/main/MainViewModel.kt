package com.cinetech.ui.screen.main

import com.cinetech.ui.base.BaseViewModel
import com.cinetech.ui.screen.main.model.MainUiEffect
import com.cinetech.ui.screen.main.model.MainUiEvent
import com.cinetech.ui.screen.main.model.MainUiReducer
import com.cinetech.ui.screen.main.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : BaseViewModel<MainUiState, MainUiEvent, MainUiEffect>(
    initialState = MainUiState(),
    reducer = MainUiReducer(),
) {

    fun onSearchTextChange(newText: String) {
        sendEventForEffect(MainUiEvent.OnSearchTextChange(newText))
    }

}