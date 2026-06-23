package com.zx_tole.pocketpsychologist.data.repository

import android.content.Context
import androidx.room.Room
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import kotlinx.coroutines.flow.Flow

class MoodRepositoryImpl(private val context: Context) : MoodRepository {
    
    private val db: MoodDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            MoodDatabase::class.java,
            "mood_database"
        ).build()
    }
    
    override suspend fun recordMood(moodRecord: MoodRecord) {
        db.moodDao().insert(moodRecord)
    }
    
    override suspend fun getLastMood(): MoodRecord? {
        return db.moodDao().getLastMood()
    }
    
    override fun getAllMoods(): Flow<List<MoodRecord>> {
        return db.moodDao().getAllMoods()
    }
    
    override fun getMoodsForDate(date: Long): Flow<List<MoodRecord>> {
        return db.moodDao().getMoodsForDate(date)
    }
    
    override suspend fun clearAll() {
        db.moodDao().clearAll()
    }
}
