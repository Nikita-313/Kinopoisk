package com.cinetech.domain.models

data class LinkedMovie(
    val id: Long,
    val name: String? = null,
    val year: Int? = null,
    val preViewUrl: String? = null,
    val kpRating: Double? = null,
)
