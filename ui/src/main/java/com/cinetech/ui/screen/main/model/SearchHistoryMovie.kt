package com.cinetech.ui.screen.main.model

import com.cinetech.domain.models.PreviewMovie

data class SearchHistoryMovie(
    override val searchTimeMs: Long,
    val previewMovie: PreviewMovie,
) : SearchHistory()