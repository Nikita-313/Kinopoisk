package com.cinetech.data.network.model

data class ApiSearchMovieResponse(
    val docs: List<ApiSearchMovieDto>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)
