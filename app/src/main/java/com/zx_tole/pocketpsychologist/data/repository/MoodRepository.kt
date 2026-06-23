package com.zx_tole.pocketpsychologist.data.repository

import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import kotlinx.coroutines.flow.Flow

interface MoodRepository {
    suspend fun recordMood(moodRecord: MoodRecord)
    suspend fun getLastMood(): MoodRecord?
    fun getAllMoods(): Flow<List<MoodRecord>>
    fun getMoodsForDate(date: Long): Flow<List<MoodRecord>>
    suspend fun clearAll()
}
