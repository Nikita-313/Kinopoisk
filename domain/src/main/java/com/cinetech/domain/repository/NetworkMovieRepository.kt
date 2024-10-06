package com.cinetech.domain.repository

import com.cinetech.domain.models.SearchMovieParam
import com.cinetech.domain.models.LoadMoviesResponse
import com.cinetech.domain.models.Response
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchMoviesByNameParam
import kotlinx.coroutines.flow.Flow

interface NetworkMovieRepository {
    fun searchMovieByName(param: SearchMoviesByNameParam): Flow<Response<out SearchMoviePageable>>
    fun searchMovie(param: SearchMovieParam): Flow<Response<out SearchMoviePageable>>
}