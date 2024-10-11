package com.cinetech.ui.screen.film.model

import android.graphics.drawable.Drawable
import com.cinetech.domain.models.Movie
import com.cinetech.ui.base.Reducer

data class FilmUiState(
    val movie: Movie? = null,
    val poster: Drawable? = null,
    val isLoading: Boolean = false,
) : Reducer.ViewState