package com.cinetech.domain.models

data class LoadCommentsParam(
    val movieId: Int,
    val page: Int = 1,
    val limitNumber: Int = 10,
)
