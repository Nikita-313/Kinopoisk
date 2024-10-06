package com.cinetech.data.local.search_history.movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchHistoryMovieDao.TABLE_NAME)
class SearchHistoryMovieEntity(
    @PrimaryKey
    val id: Long,
    val name: String,
    val alternativeName: String,
    val countries: List<String>,
    val year: Int,
    val ageRating: Int,
    val previewUrl: String,
    val kpRating: Double,
    val searchTimeMs: Long,
)