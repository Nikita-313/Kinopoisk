package com.cinetech.domain.models

sealed class SearchHistory {
    abstract val searchTimeMs: Long

    data class Movie(
        override val searchTimeMs: Long,
        val previewMovie: PreviewMovie,
    ) : SearchHistory()

    data class Text(
        override val searchTimeMs: Long,
        val text:String,
    ) : SearchHistory()
}