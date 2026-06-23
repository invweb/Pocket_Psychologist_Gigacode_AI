package com.zx_tole.pocketpsychologist.ui.home

import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import com.zx_tole.pocketpsychologist.data.model.MoodType

// State represents the UI state
data class HomeState(
    val moodHistory: List<MoodRecord> = emptyList(),
    val lastMood: MoodType? = null,
    val isRecording: Boolean = false,
    val analysisProgress: Float = 0f,
    val recordDuration: Long = 0L,
    val isLoadingHistory: Boolean = true
)

// Intent represents user actions
sealed interface HomeIntent {
    object RecordButtonClick : HomeIntent
    object StopButtonClick : HomeIntent
    object StartRecording : HomeIntent
    object StopRecording : HomeIntent
    object CancelAnalysis : HomeIntent
    object AnalyzeVoice : HomeIntent
    object LoadHistory : HomeIntent
}

// Event represents UI events (side effects)
sealed interface HomeEvent {
    data class RecordingStarted(val duration: Long) : HomeEvent
    data class RecordingStopped(val duration: Long) : HomeEvent
    data class AnalysisProgressChanged(val progress: Float) : HomeEvent
    data class MoodAnalyzed(val record: MoodRecord) : HomeEvent
    data class HistoryLoaded(val records: List<MoodRecord>, val lastMood: MoodType?) : HomeEvent
    data class Error(val message: String) : HomeEvent
}
