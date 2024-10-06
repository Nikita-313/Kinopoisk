package com.cinetech.domain.models

data class SearchMovie(
    val id: Long,
    val name: String,
    val enName: String?,
    val year: Int,
    val country: String?,
    val ageRating: Int,
    val previewUrl: String?,
    val kpRating: Double?
)