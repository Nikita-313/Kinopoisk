package com.cinetech.domain.models

data class SearchMoviesParam(
    val movieName: String,
    val page: Int = 1,
    val limitNumber: Int = 10,
)
