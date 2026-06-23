package com.zx_tole.pocketpsychologist.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import com.zx_tole.pocketpsychologist.data.model.MoodType
import com.zx_tole.pocketpsychologist.data.repository.MoodRepository
import com.zx_tole.pocketpsychologist.voice.VoiceAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoodRepository,
    private val voiceAnalyzer: VoiceAnalyzer
) : ViewModel() {
    
    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state
    
    private val _events = MutableStateFlow<HomeEvent?>(null)
    val events: StateFlow<HomeEvent?> = _events
    
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val ioScope = CoroutineScope(Dispatchers.IO)
    private var analysisJob: Job? = null
    private var recordingDurationJob: Job? = null
    private var audioLevelJob: Job? = null
    
    init {
        loadHistory()
    }
    
    fun handleIntent(intent: HomeIntent) {
        val (newState, events) = HomeReducer.reduce(_state.value, intent)
        _state.update { newState }
        
        // Process events
        for (event in events) {
            _events.value = event
            processEvent(event)
        }
    }
    
    private fun processEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.RecordingStarted -> {
                startRecording("/sdcard/record.mp4")
            }
            is HomeEvent.RecordingStopped -> {
                stopRecording()
                startAnalysis()
            }
            is HomeEvent.AnalysisProgressChanged -> {
                // Progress is handled in reducer
            }
            is HomeEvent.MoodAnalyzed -> {
                mainScope.launch {
                    repository.recordMood(event.record)
                }
            }
            is HomeEvent.HistoryLoaded -> {
                // Already processed in reducer
            }
            is HomeEvent.Error -> {
                // Handle error
            }
        }
    }
    
    private fun startRecording(outputPath: String, maxDuration: Long = 30000) {
        voiceAnalyzer.startRecording(outputPath)
        
        // Start recording duration timer
        recordingDurationJob?.cancel()
        recordingDurationJob = mainScope.launch {
            while (_state.value.isRecording) {
                val currentDuration = _state.value.recordDuration + 100
                _state.update { it.copy(recordDuration = currentDuration) }
                delay(100)
            }
        }
        
        // Auto-stop after max duration
        mainScope.launch {
            delay(maxDuration)
            if (_state.value.isRecording) {
                handleIntent(HomeIntent.StopButtonClick)
            }
        }
    }
    
    private fun stopRecording() {
        voiceAnalyzer.stopRecording()
        recordingDurationJob?.cancel()
        audioLevelJob?.cancel()
    }
    
    private fun startAnalysis() {
        analysisJob?.cancel()
        analysisJob = ioScope.launch {
            delay(1500) // Simulate analysis time
            handleIntent(HomeIntent.AnalyzeVoice)
            
            // Get audio characteristics
            val audioLevel = voiceAnalyzer.getAudioLevel()
            val speechRate = voiceAnalyzer.estimateSpeechRate()
            val pitch = voiceAnalyzer.estimatePitch(audioLevel)
            
            // Determine mood based on characteristics
            val moodType = voiceAnalyzer.determineMood(audioLevel, speechRate, pitch)
            
            val moodRecord = MoodRecord(
                moodType = moodType,
                speechRate = speechRate,
                pitch = pitch,
                volume = audioLevel,
                confidence = 0.8f + (Math.random() * 0.2f).toFloat()
            )
            
            mainScope.launch {
                _events.value = HomeEvent.MoodAnalyzed(moodRecord)
            }
        }
    }
    
    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllMoods().collect { records ->
                val lastMood = records.firstOrNull()
                _events.value = HomeEvent.HistoryLoaded(records, lastMood?.moodType)
            }
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        voiceAnalyzer.release()
        analysisJob?.cancel()
        recordingDurationJob?.cancel()
        audioLevelJob?.cancel()
    }
}
