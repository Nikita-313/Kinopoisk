package com.cinetech.domain.models

data class LoadMoviesResponse(
    val docs: List<PreviewMovie>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)