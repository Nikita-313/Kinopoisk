package com.cinetech.data.network.model

data class ApiLoadMoviesResponse(
    val docs: List<ApiMovieDto>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)

