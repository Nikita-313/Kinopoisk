package com.cinetech.data.repository

import com.cinetech.data.local.AppDatabase
import com.cinetech.data.mapping.toDomain
import com.cinetech.data.mapping.toEntity
import com.cinetech.domain.models.SearchHistory
import com.cinetech.domain.repository.LocalSearchHistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalSearchHistoryRepositoryImpl @Inject constructor(
    private val database: AppDatabase
) : LocalSearchHistoryRepository {
    override fun subscribeTextSearchHistory(): Flow<List<SearchHistory.Text>> = database.searchHistoryTextDao().getAll().map { history -> history.map { it.toDomain() } }

    override fun subscribeMoviesSearchHistory(): Flow<List<SearchHistory.Movie>> = database.searchHistoryMovieDao().getAll().map { history -> history.map { it.toDomain() } }

    override suspend fun saveMoviesSearchHistory(movie: SearchHistory.Movie) = withContext(Dispatchers.IO) {
        database.searchHistoryMovieDao().upsert(movie.toEntity())
    }

    override suspend fun saveTextSearchHistory(text: SearchHistory.Text) = withContext(Dispatchers.IO) {
        database.searchHistoryTextDao().upsert(text.toEntity())
    }

    override suspend fun deleteMoviesSearchHistory(movieId: Long) = withContext(Dispatchers.IO) {
        database.searchHistoryMovieDao().deleteById(movieId)
    }

    override suspend fun deleteTextSearchHistory(text: String) = withContext(Dispatchers.IO) {
        database.searchHistoryTextDao().deleteByText(text)
    }
}