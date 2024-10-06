package com.cinetech.domain.models

data class Comment(
    val id: Int,
    val title: String?,
    val type: String?,
    val review: String?,
    val date: String?,
    val author: String?,
)
