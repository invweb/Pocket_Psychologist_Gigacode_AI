package com.zx_tole.pocketpsychologist.ui.home

import com.zx_tole.pocketpsychologist.data.model.MoodRecord

// Reducer processes intents and updates state
object HomeReducer {
    fun reduce(state: HomeState, intent: HomeIntent): Pair<HomeState, List<HomeEvent>> {
        return when (intent) {
            is HomeIntent.StartRecording -> {
                val newState = state.copy(
                    isRecording = true,
                    recordDuration = 0L
                )
                newState to listOf(HomeEvent.RecordingStarted(0L))
            }
            
            is HomeIntent.StopRecording -> {
                val newState = state.copy(
                    isRecording = false,
                    analysisProgress = 0f
                )
                newState to listOf(HomeEvent.RecordingStopped(state.recordDuration))
            }
            
            is HomeIntent.CancelAnalysis -> {
                val newState = state.copy(
                    analysisProgress = 0f
                )
                newState to emptyList()
            }
            
            is HomeIntent.AnalyzeVoice -> {
                val newState = state.copy(
                    analysisProgress = 0.3f
                )
                newState to listOf(HomeEvent.AnalysisProgressChanged(0.3f))
            }
            
            is HomeIntent.LoadHistory -> {
                // This intent doesn't change state directly
                // Events are handled separately
                state to emptyList()
            }
            
            is HomeIntent.RecordButtonClick -> {
                // If not recording, start recording
                if (!state.isRecording) {
                    val newState = state.copy(
                        isRecording = true,
                        recordDuration = 0L
                    )
                    newState to listOf(HomeEvent.RecordingStarted(0L))
                } else {
                    state to emptyList()
                }
            }
            
            is HomeIntent.StopButtonClick -> {
                // If recording, stop recording
                if (state.isRecording) {
                    val newState = state.copy(
                        isRecording = false,
                        analysisProgress = 0f
                    )
                    newState to listOf(HomeEvent.RecordingStopped(state.recordDuration))
                } else {
                    state to emptyList()
                }
            }
        }
    }
    
    // Update state based on events (side effects)
    fun updateState(state: HomeState, event: HomeEvent): HomeState {
        return when (event) {
            is HomeEvent.RecordingStarted -> state.copy(
                isRecording = true,
                recordDuration = 0L
            )
            
            is HomeEvent.RecordingStopped -> state.copy(
                isRecording = false,
                analysisProgress = 0f
            )
            
            is HomeEvent.AnalysisProgressChanged -> state.copy(
                analysisProgress = event.progress
            )
            
            is HomeEvent.MoodAnalyzed -> {
                val newHistory = listOf(event.record) + state.moodHistory.filter { it != event.record }
                state.copy(
                    moodHistory = newHistory,
                    lastMood = event.record.moodType,
                    analysisProgress = 0f
                )
            }
            
            is HomeEvent.HistoryLoaded -> state.copy(
                moodHistory = event.records,
                lastMood = event.lastMood,
                isLoadingHistory = false
            )
            
            is HomeEvent.Error -> state
        }
    }
}
