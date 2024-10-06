package com.cinetech.data.local.search_history.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryMovieDao {

    @Upsert
    fun upsert(searchHistoryMovieEntity: SearchHistoryMovieEntity)

    @Query("DELETE from $TABLE_NAME WHERE id=:id")
    fun deleteById(id: Long)

    @Query("SELECT * from $TABLE_NAME ")
    fun getAll(): Flow<List<SearchHistoryMovieEntity>>

    companion object {
        const val TABLE_NAME = "SearchHistoryMovie"
    }
}