package com.zx_tole.pocketpsychologist.data.repository

import androidx.room.*
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface MoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(moodRecord: MoodRecord)

    @Update
    suspend fun update(moodRecord: MoodRecord)

    @Delete
    suspend fun delete(moodRecord: MoodRecord)

    @Query("SELECT * FROM mood_records ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMood(): MoodRecord?

    @Query("SELECT * FROM mood_records ORDER BY timestamp DESC LIMIT 10")
    fun getAllMoods(): Flow<List<MoodRecord>>

    @Query("SELECT * FROM mood_records WHERE date(timestamp, 'unixepoch') = date(:date, 'unixepoch')")
    fun getMoodsForDate(date: Long): Flow<List<MoodRecord>>

    @Query("SELECT COUNT(*) FROM mood_records")
    suspend fun getCount(): Int

    @Query("DELETE FROM mood_records")
    suspend fun clearAll()
}
