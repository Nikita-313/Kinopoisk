package com.cinetech.domain.models

data class SearchMoviePageable(
    val docs: List<SearchMovie>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)
