package com.cinetech.data.local.search_history.movie

import androidx.room.Dao

@Dao
interface SearchHistoryMovieDao {


    companion object{
        const val TABLE_NAME = "SearchHistoryMovie"
    }
}