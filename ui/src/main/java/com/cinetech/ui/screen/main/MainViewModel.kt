package com.cinetech.ui.screen.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.Response
import com.cinetech.domain.models.SearchHistory
import com.cinetech.domain.models.SearchMoviesByNameParam
import com.cinetech.domain.repository.LocalSearchHistoryRepository
import com.cinetech.domain.repository.NetworkMovieRepository
import com.cinetech.ui.base.BaseViewModel
import com.cinetech.ui.screen.main.model.MainUiEffect
import com.cinetech.ui.screen.main.model.MainUiEvent
import com.cinetech.ui.screen.main.model.MainUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val networkMovieRepository: NetworkMovieRepository,
    private val localSearchHistoryRepository: LocalSearchHistoryRepository
) : BaseViewModel<MainUiState, MainUiEvent, MainUiEffect>(
    initialState = MainUiState(),
    reducer = MainUiReducer(),
) {

    private var searchMovieJob: Job? = null

    init {
        subscribeLocalSearchHistory()
    }

    fun onSearchTextChange(newText: String) {
        sendEvent(MainUiEvent.OnSearchTextChange(newText))
        searchMovie(newText)
    }

    fun saveTextHistory(text: String) {
        val textForSave = SearchHistory.Text(text = text, searchTimeMs = System.currentTimeMillis())
        viewModelScope.launch {
            localSearchHistoryRepository.saveTextSearchHistory(textForSave)
        }
    }

    fun saveMovieHistory(movie: PreviewMovie) {
        val movieForSave = SearchHistory.Movie(previewMovie = movie, searchTimeMs = System.currentTimeMillis())
        viewModelScope.launch {
            localSearchHistoryRepository.saveMoviesSearchHistory(movieForSave)
        }
    }

    fun deleteTextHistory(text: String) {
        viewModelScope.launch {
            localSearchHistoryRepository.deleteTextSearchHistory(text)
        }
    }

    fun deleteMovieHistory(movieId: Long) {
        viewModelScope.launch {
            localSearchHistoryRepository.deleteMoviesSearchHistory(movieId)
        }
    }

    private fun searchMovie(name: String) {

        searchMovieJob?.cancel()
        sendEvent(MainUiEvent.MoviesLoading(false))

        if (name == "") {
            sendEvent(MainUiEvent.UpdateMovies(null))
            return
        }

        searchMovieJob = viewModelScope.launch {
            delay(500)
            val searchMovieParams = SearchMoviesByNameParam(movieName = name)
            networkMovieRepository.searchMovieByName(searchMovieParams).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.e("MainViewModel searchMovie", response.throwable.toString())
                        sendEvent(MainUiEvent.MoviesLoading(false))
                    }

                    Response.Loading -> {
                        sendEvent(MainUiEvent.MoviesLoading(true))
                    }

                    is Response.Success -> {
                        sendEvent(MainUiEvent.MoviesLoading(false))
                        sendEventForEffect(MainUiEvent.UpdateMovies(response.result))
                    }

                    Response.Timeout -> {
                        sendEvent(MainUiEvent.MoviesLoading(false))
                    }
                }
            }
        }
    }

    private fun subscribeLocalSearchHistory() {
        viewModelScope.launch {
            localSearchHistoryRepository.subscribeTextSearchHistory().collect {
                sendEvent(MainUiEvent.UpdateSearchHistoryText(it))
            }
        }
        viewModelScope.launch {
            localSearchHistoryRepository.subscribeMoviesSearchHistory().collect {
                sendEvent(MainUiEvent.UpdateSearchHistoryMovie(it))
            }
        }

    }

}