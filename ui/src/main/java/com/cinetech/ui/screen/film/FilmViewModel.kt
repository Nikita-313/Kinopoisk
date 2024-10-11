package com.cinetech.ui.screen.film

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import coil.ImageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.cinetech.domain.repository.NetworkMovieRepository
import com.cinetech.domain.utils.Response
import com.cinetech.ui.base.BaseAndroidViewModel
import com.cinetech.ui.navigation.Screen
import com.cinetech.ui.screen.film.model.FilmUiEffect
import com.cinetech.ui.screen.film.model.FilmUiEvent
import com.cinetech.ui.screen.film.model.FilmUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilmViewModel @Inject constructor(
    savedState: SavedStateHandle,
    application: Application,
    private val networkMovieRepository: NetworkMovieRepository,
) : BaseAndroidViewModel<FilmUiState, FilmUiEvent, FilmUiEffect>(
    initialState = FilmUiState(),
    reducer = FilmUiReducer(),
    application = application
) {

    private val movieId = savedState.toRoute<Screen.Film>().id


    init {
        fetchMovie()
    }


    private fun fetchMovie() {
        viewModelScope.launch {
            networkMovieRepository.getMovieById(movieId).collect { response ->
                when (response) {
                    Response.Loading -> {
                        sendEvent(FilmUiEvent.UpdateLoading(true))
                    }

                    is Response.Success -> {
                        val poster = loadImage(response.result.posterUrl)
                        sendEvent(FilmUiEvent.UpdateMovie(response.result,poster))
                        sendEvent(FilmUiEvent.UpdateLoading(false))
                    }

                    is Response.Error -> {
                        sendEvent(FilmUiEvent.UpdateLoading(false))
                        Log.e("FilmViewModel fetchMovie", response.throwable.toString())
                    }

                    Response.Timeout -> {
                        sendEvent(FilmUiEvent.UpdateLoading(false))
                    }
                }

            }
        }
    }

    private suspend fun loadImage(url:String?): Drawable? {
        if(url == null) return null

        val imageLoader = ImageLoader.Builder(getApplication()).build()
        val request = ImageRequest.Builder(context = getApplication())
            .data(url)
            .allowHardware(false)
            .build()

        val imageResult = imageLoader.execute(request)

        if (imageResult is SuccessResult) {
            return imageResult.drawable
        } else if (imageResult is ErrorResult) {
            Log.e("FilmViewModel loadImage", imageResult.toString())
            return null
        }

        return imageResult.drawable
    }
}