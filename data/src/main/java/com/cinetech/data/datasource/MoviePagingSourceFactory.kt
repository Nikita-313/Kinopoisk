package com.cinetech.data.datasource

import com.cinetech.data.network.MovieService
import javax.inject.Inject

class MoviePagingSourceFactory @Inject constructor(
    private val movieService: MovieService,
) {
    fun create(movieName: String): MoviePagingSource {
        return MoviePagingSource(movieName, movieService)
    }
}