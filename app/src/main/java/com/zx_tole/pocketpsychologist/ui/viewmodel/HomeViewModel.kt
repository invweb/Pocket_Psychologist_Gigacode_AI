package com.zx_tole.pocketpsychologist.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import com.zx_tole.pocketpsychologist.data.model.MoodType
import com.zx_tole.pocketpsychologist.data.repository.MoodRepository
import com.zx_tole.pocketpsychologist.voice.VoiceAnalyzer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: MoodRepository,
    private val voiceAnalyzer: VoiceAnalyzer
) : ViewModel() {
    
    private val _isRecording = MutableStateFlow(false)
    val isRecording: StateFlow<Boolean> = _isRecording.asStateFlow()
    
    private val _analysisProgress = MutableStateFlow(0f)
    val analysisProgress: StateFlow<Float> = _analysisProgress.asStateFlow()
    
    val recordDuration = MutableStateFlow(0L)
    
    private val _moodFlow = MutableStateFlow<List<MoodRecord>>(emptyList())
    val moodFlow: StateFlow<List<MoodRecord>> = _moodFlow.asStateFlow()
    
    private val _lastMoodFlow = MutableStateFlow<MoodType?>(null)
    val lastMoodFlow: StateFlow<MoodType?> = _lastMoodFlow.asStateFlow()
    
    init {
        loadHistory()
        loadLastMood()
    }
    
    private fun loadHistory() {
        viewModelScope.launch {
            repository.getAllMoods().collect { records ->
                _moodFlow.value = records
            }
        }
    }
    
    private fun loadLastMood() {
        viewModelScope.launch {
            val lastMood = repository.getLastMood()
            _lastMoodFlow.value = lastMood?.moodType
        }
    }
    
    fun startRecording() {
        recordDuration.value = 0L
        _isRecording.value = true
        
        // Start recording duration timer
        viewModelScope.launch {
            while (_isRecording.value) {
                recordDuration.value += 100
                delay(100)
            }
        }
        
        voiceAnalyzer.startRecording("/sdcard/record.mp4")
    }
    
    fun stopRecording() {
        voiceAnalyzer.stopRecording()
        _isRecording.value = false
    }
    
    fun analyzeVoice() {
        // Trigger analysis - will be called automatically when recording stops
        // This provides a manual override if needed
    }
    
    fun cancelAnalysis() {
        voiceAnalyzer.cancelAnalysis()
    }
    
    override fun onCleared() {
        super.onCleared()
        voiceAnalyzer.release()
    }
}
