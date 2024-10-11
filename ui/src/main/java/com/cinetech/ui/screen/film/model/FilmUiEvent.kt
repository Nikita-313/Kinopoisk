package com.cinetech.ui.screen.film.model

import android.graphics.drawable.Drawable
import com.cinetech.domain.models.Movie
import com.cinetech.ui.base.Reducer

sealed class FilmUiEvent : Reducer.ViewEvent {
    class UpdateMovie(
        val movie: Movie,
        val poster: Drawable?,
    ) : FilmUiEvent()

    class UpdateLoading(val isLoading: Boolean) : FilmUiEvent()
}