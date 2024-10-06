package com.cinetech.domain.repository

import com.cinetech.domain.models.Response
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchMoviesParam
import kotlinx.coroutines.flow.Flow

interface NetworkMovieRepository {
    fun searchMovie(param: SearchMoviesParam): Flow<Response<out SearchMoviePageable>>
}