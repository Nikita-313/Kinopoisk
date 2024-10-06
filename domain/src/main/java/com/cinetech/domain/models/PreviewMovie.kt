package com.cinetech.domain.models

data class PreviewMovie(
    val id: Long,
    val name: String,
    val alternativeName: String,
    val countries: List<String>,
    val year: Int,
    val ageRating: Int,
    val previewUrl: String,
    val kpRating: Double
)