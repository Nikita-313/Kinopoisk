package com.cinetech.domain.repository

import androidx.paging.PagingData
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.SearchMovieParam
import com.cinetech.domain.utils.Response
import com.cinetech.domain.models.SearchMoviePageable
import com.cinetech.domain.models.SearchMoviesByNameParam
import kotlinx.coroutines.flow.Flow

interface NetworkMovieRepository {
    fun searchMovieByName(param: SearchMoviesByNameParam): Flow<Response<out SearchMoviePageable>>
    fun searchMovie(param: SearchMovieParam): Flow<Response<out SearchMoviePageable>>
    fun searchMoviesPaging(query: String, pageSize: Int = 10): Flow<PagingData<PreviewMovie>>
}