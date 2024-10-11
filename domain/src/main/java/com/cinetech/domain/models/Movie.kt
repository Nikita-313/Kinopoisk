package com.cinetech.domain.models

data class Movie(
    val id: Long,
    val name: String,
    val year: Int,
    val ageRating: Int,
    val countries: List<String>,
    val posterUrl: String?,
    val kpRating: Double,
    val shortDescription: String,
    val description: String,
    val similarMovies: List<LinkedMovie>,
    val kpVotesNumber: Int,
    val enName: String,
    val movieLength:Int,
)

