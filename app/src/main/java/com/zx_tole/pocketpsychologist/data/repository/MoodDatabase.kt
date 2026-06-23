package com.zx_tole.pocketpsychologist.data.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zx_tole.pocketpsychologist.data.model.MoodRecord

@Database(
    entities = [MoodRecord::class],
    version = 1,
    exportSchema = false
)
abstract class MoodDatabase : RoomDatabase() {
    abstract fun moodDao(): MoodDao
    
    companion object {
        const val DATABASE_NAME = "mood_database"
    }
}
