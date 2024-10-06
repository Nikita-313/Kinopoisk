package com.cinetech.data.network.model

data class ApiSearchMovieDto(
    val id: Long,
    val name: String,
    val enName: String?,
    val year: Int,
    val country: String?,
    val ageRating: Int,
    val poster: ApiPoster,
    val rating: ApiRating?,
)
