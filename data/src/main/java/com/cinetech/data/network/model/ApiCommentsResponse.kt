package com.cinetech.data.network.model


data class ApiCommentsResponse(
    val docs: List<ApiComment>,
    val total: Int,
    val limit: Int,
    val page: Int,
    val pages: Int,
)
