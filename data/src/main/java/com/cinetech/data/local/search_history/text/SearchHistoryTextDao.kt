package com.cinetech.data.local.search_history.text

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryTextDao {

    @Upsert
    fun upsert(searchHistoryTextEntity: SearchHistoryTextEntity)

    @Query("DELETE from $TABLE_NAME WHERE text=:text")
    fun deleteByText(text: String)

    @Query("SELECT * from $TABLE_NAME ")
    fun getAll(): Flow<List<SearchHistoryTextEntity>>

    companion object{
        const val TABLE_NAME = "SearchHistoryText"
    }
}