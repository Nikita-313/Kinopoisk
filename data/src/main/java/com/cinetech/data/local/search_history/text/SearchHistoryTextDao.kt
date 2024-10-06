package com.cinetech.data.local.search_history.text

import androidx.room.Dao

@Dao
interface SearchHistoryTextDao {

    companion object{
        const val TABLE_NAME = "SearchHistoryText"
    }
}