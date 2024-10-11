package com.cinetech.ui.screen.film

import com.cinetech.ui.base.Reducer
import com.cinetech.ui.screen.film.model.FilmUiEffect
import com.cinetech.ui.screen.film.model.FilmUiEvent
import com.cinetech.ui.screen.film.model.FilmUiState

class FilmUiReducer:Reducer<FilmUiState,FilmUiEvent,FilmUiEffect> {
    override fun reduce(previousState: FilmUiState, event: FilmUiEvent): Pair<FilmUiState, FilmUiEffect?> {
         return when(event){

             is FilmUiEvent.UpdateMovie -> {
                 previousState.copy(movie = event.movie, poster = event.poster) to null
             }

             is FilmUiEvent.UpdateLoading -> {
                 previousState.copy(isLoading = event.isLoading) to null
             }
         }
    }
}