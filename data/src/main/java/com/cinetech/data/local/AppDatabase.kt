package com.cinetech.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.cinetech.data.local.search_history.movie.ListStringConvector
import com.cinetech.data.local.search_history.movie.SearchHistoryMovieDao
import com.cinetech.data.local.search_history.movie.SearchHistoryMovieEntity
import com.cinetech.data.local.search_history.text.SearchHistoryTextDao
import com.cinetech.data.local.search_history.text.SearchHistoryTextEntity

@Database(
    entities = [
        SearchHistoryMovieEntity::class,
        SearchHistoryTextEntity::class,
    ],
    version = 1,
    exportSchema = true,
    autoMigrations = [],
)
@TypeConverters(ListStringConvector::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryMovieDao(): SearchHistoryMovieDao
    abstract fun searchHistoryTextDao(): SearchHistoryTextDao
}