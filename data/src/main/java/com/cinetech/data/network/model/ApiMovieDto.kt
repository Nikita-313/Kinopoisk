package com.cinetech.data.network.model

data class ApiMovieDto(
    val id: Long,
    val name: String?,
    val alternativeName: String?,
    val year: Int?,
    val ageRating: Int?,
    val countries: List<ApiCountry>?,
    val poster: ApiPoster?,
    val rating: ApiRating?,
    val shortDescription: String?,
    val description: String?,
    val similarMovies: List<ApiLinkedMovie>?,
    val votes: ApiVotes?,
    val enName: String?,
    val movieLength:Int?,
)
