package com.cinetech.ui.screen.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.cinetech.domain.models.Response
import com.cinetech.domain.models.SearchMoviesByNameParam
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
    private val networkMovieRepository: NetworkMovieRepository
) : BaseViewModel<MainUiState, MainUiEvent, MainUiEffect>(
    initialState = MainUiState(),
    reducer = MainUiReducer(),
) {

    private var searchMovieJob: Job? = null

    fun onSearchTextChange(newText: String) {
        sendEvent(MainUiEvent.OnSearchTextChange(newText))
        searchMovie(newText)
    }

    private fun searchMovie(name: String) {

        searchMovieJob?.cancel()
        sendEvent(MainUiEvent.MoviesLoading(false))

        if(name == "") {
            sendEvent(MainUiEvent.UpdateMovies(null))
            return
        }

        searchMovieJob = viewModelScope.launch {
            delay(500)
            val searchMovieParams = SearchMoviesByNameParam(movieName = name)
            networkMovieRepository.searchMovieByName(searchMovieParams).collect { response ->
                when (response) {
                    is Response.Error -> {
                        Log.e("MainViewModel searchMovie",response.throwable.toString())
                        sendEvent(MainUiEvent.MoviesLoading(false))
                    }

                    Response.Loading -> {
                        sendEvent(MainUiEvent.MoviesLoading(true))
                    }

                    is Response.Success -> {
                        sendEvent(MainUiEvent.MoviesLoading(false))
                        sendEvent(MainUiEvent.UpdateMovies(response.result))
                    }
                    Response.Timeout -> {
                        sendEvent(MainUiEvent.MoviesLoading(false))
                    }
                }
            }
        }
    }

}