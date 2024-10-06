package com.cinetech.domain.repository

import com.cinetech.domain.models.SearchHistory
import kotlinx.coroutines.flow.Flow

interface LocalSearchHistoryRepository {
    fun subscribeTextSearchHistory(): Flow<List<SearchHistory.Text>>
    fun subscribeMoviesSearchHistory(): Flow<List<SearchHistory.Movie>>

    suspend fun saveMoviesSearchHistory(movie: SearchHistory.Movie)
    suspend fun saveTextSearchHistory(text: SearchHistory.Text)

    suspend fun deleteMoviesSearchHistory(movieId: Long)
    suspend fun deleteTextSearchHistory(text: String)
}