package com.zx_tole.pocketpsychologist.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_records")
data class MoodRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val moodType: MoodType,
    val timestamp: Long = System.currentTimeMillis(),
    val analysisDuration: Long = 0,
    val speechRate: Float = 0f,
    val pitch: Float = 0f,
    val volume: Float = 0f,
    val confidence: Float = 0f
)
