package com.cinetech.domain.models

data class LoadMoviesParam(
    val page: Int = 1,
    val limitNumber: Int = 10,
    val countries: List<String>? = null,
    val ageRantingRange: AgeRantingRange? = null,
    val yearRange: YearRange? = null,
)
