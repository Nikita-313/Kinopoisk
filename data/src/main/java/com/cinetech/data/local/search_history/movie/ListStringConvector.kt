package com.cinetech.data.local.search_history.movie

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConvector {

    @TypeConverter
    fun fromListStringToString(list: List<String>): String = Gson().toJson(list)

    @TypeConverter
    fun toListStringFromString(stringList: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(stringList, type)
    }

}