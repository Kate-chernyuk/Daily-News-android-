package com.thatgame.dailynews.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.thatgame.dailynews.Incident

@Database(entities = [Incident::class], version = 1, exportSchema = false)
@TypeConverters(IncidentTypeConverters::class)
abstract class IncidentDatabase: RoomDatabase() {

    abstract fun IncidentDao(): IncidentDAO
}