package com.cinetech.data.mapping

import com.cinetech.data.local.search_history.movie.SearchHistoryMovieEntity
import com.cinetech.data.local.search_history.text.SearchHistoryTextEntity
import com.cinetech.domain.models.PreviewMovie
import com.cinetech.domain.models.SearchHistory

fun SearchHistoryTextEntity.toDomain(): SearchHistory.Text {
    return SearchHistory.Text(
        searchTimeMs = searchTimeMs,
        text = text
    )
}

fun SearchHistory.Text.toEntity(): SearchHistoryTextEntity {
    return SearchHistoryTextEntity(
        searchTimeMs = searchTimeMs,
        text = text
    )
}


fun SearchHistoryMovieEntity.toDomain(): SearchHistory.Movie {
    return SearchHistory.Movie(
        searchTimeMs = searchTimeMs,
        previewMovie = PreviewMovie(
            id = id,
            name = name,
            alternativeName = alternativeName,
            countries = countries,
            year = year,
            ageRating = ageRating,
            previewUrl = previewUrl,
            kpRating = kpRating
        )
    )
}

fun SearchHistory.Movie.toEntity(): SearchHistoryMovieEntity {
    return SearchHistoryMovieEntity(
        searchTimeMs = searchTimeMs,
        id = previewMovie.id,
        name = previewMovie.name,
        alternativeName = previewMovie.alternativeName,
        countries = previewMovie.countries,
        year = previewMovie.year,
        ageRating = previewMovie.ageRating,
        previewUrl = previewMovie.previewUrl,
        kpRating = previewMovie.kpRating
    )
}