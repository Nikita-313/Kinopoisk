package com.cinetech.domain.models

data class SearchMoviesByNameParam(
    val movieName: String,
    val page: Int = 1,
    val limitNumber: Int = 10,
)
