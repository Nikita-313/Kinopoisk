package com.cinetech.domain.models

data class Movie(
    val id: Long? = null,
    val name: String? = null,
    val year: Int? = null,
    val ageRating: Int? = null,
    val countries: List<String>? = null,
    val posterUrl: String? = null,
    val kpRating: Double? = null,
    val shortDescription: String? = null,
    val description: String? = null,
    val similarMovies: List<LinkedMovie>? = null,
    val kpVotesNumber:Int? = null,
)
