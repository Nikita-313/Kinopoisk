package com.cinetech.ui.screen.main.model

import com.cinetech.domain.models.SearchHistory
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.ui.base.Reducer

sealed class MainUiEvent : Reducer.ViewEvent {
    class OnSearchTextChange(val newText: String) : MainUiEvent()
    class UpdateMovies(val movies: SearchMoviePageable?) : MainUiEvent()
    class UpdateSearchHistoryMovie(val searchHistory: List<SearchHistory.Movie> ) : MainUiEvent()
    class UpdateSearchHistoryText(val searchHistory: List<SearchHistory.Text> ) : MainUiEvent()
    class MoviesLoading(val isLoading: Boolean) : MainUiEvent()
}