package com.cinetech.data.local.search_history.text

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchHistoryTextDao.TABLE_NAME)
data class SearchHistoryTextEntity(
    @PrimaryKey
    val text: String,
    val searchTimeMs: Long,
)
