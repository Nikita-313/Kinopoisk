package com.cinetech.domain.models

data class PreviewMovie(
    val id: Long? = null,
    val name: String? = null,
    val alternativeName: String? = null,
    val year: Int? = null,
    val ageRating: Int? = null,
    val countries: List<String>,
    val preViewUrl: String? = null,
    val kpRating: Double? = null,
)