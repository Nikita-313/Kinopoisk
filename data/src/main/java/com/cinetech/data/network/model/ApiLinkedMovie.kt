package com.cinetech.data.network.model

data class ApiLinkedMovie(
    val id: Long,
    val name: String? = null,
    val year: Int? = null,
    val poster: ApiPoster? = null,
    val rating: ApiRating? = null,
)
