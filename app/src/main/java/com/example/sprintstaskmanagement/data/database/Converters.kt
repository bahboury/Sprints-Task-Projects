package com.example.sprintstaskmanagement.data.database

import androidx.room.TypeConverter
import java.util.Date

class Converters {
    @TypeConverter
    fun fromDate(date: Date) = date.time

    @TypeConverter
    fun toDate(millisSinceEpoch: Long) = Date(millisSinceEpoch)

    @TypeConverter
    fun fromStringList(list: List<String>) = list.joinToString(",")

    @TypeConverter
    fun toStringList(string: String) = string.split(",")
}