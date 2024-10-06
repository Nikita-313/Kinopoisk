package com.cinetech.domain.models

data class CommentsResponse(
    val docs: List<Comment>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)
