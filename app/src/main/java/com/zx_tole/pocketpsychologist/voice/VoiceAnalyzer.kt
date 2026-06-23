package com.zx_tole.pocketpsychologist.voice

import android.content.Context
import android.media.MediaRecorder
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zx_tole.pocketpsychologist.data.model.MoodRecord
import com.zx_tole.pocketpsychologist.data.model.MoodType
import kotlinx.coroutines.*
import java.io.IOException
import kotlin.math.absoluteValue

class VoiceAnalyzer(private val context: Context) {
    
    private val _isRecording = MutableLiveData<Boolean>()
    val isRecording: LiveData<Boolean> = _isRecording
    
    private val _audioLevels = MutableLiveData<List<Float>>()
    val audioLevels: LiveData<List<Float>> = _audioLevels
    
    private val _analysisResult = MutableLiveData<MoodRecord>()
    val analysisResult: LiveData<MoodRecord> = _analysisResult
    
    private val _analysisProgress = MutableLiveData<Float>()
    val analysisProgress: LiveData<Float> = _analysisProgress
    
    private var mediaRecorder: MediaRecorder? = null
    private var audioLevelsJob: Job? = null
    private var analysisJob: Job? = null
    
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val ioScope = CoroutineScope(Dispatchers.IO)
    
    // Get current audio level from MediaRecorder
    fun getAudioLevel(): Float {
        return mediaRecorder?.maxAmplitude?.toFloat()?.div(32768f) ?: 0f
    }
    
    fun startRecording(outputPath: String, maxDuration: Long = 30000) {
        if (_isRecording.value == true) return
        
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile(outputPath)
            
            try {
                prepare()
            } catch (e: IOException) {
                e.printStackTrace()
                _isRecording.postValue(false)
                return
            }
            
            start()
            _isRecording.postValue(true)
        }
        
        // Start audio level monitoring
        audioLevelsJob?.cancel()
        audioLevelsJob = mainScope.launch {
            while (isActive && _isRecording.value == true) {
                val level = getAudioLevel()
                _audioLevels.postValue(listOf(level))
                delay(100)
            }
        }
        
        // Auto-stop after max duration
        mainScope.launch {
            delay(maxDuration)
            stopRecording()
        }
    }
    
    fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
                reset()
                release()
                _isRecording.postValue(false)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mediaRecorder = null
        
        audioLevelsJob?.cancel()
        
        // Start analysis
        analysisJob?.cancel()
        analysisJob = ioScope.launch {
            performAnalysis()
        }
    }
    
    private suspend fun performAnalysis() {
        // For MVP, we'll simulate analysis based on audio characteristics
        // In production, you would use ML Kit or other ML models
        
        delay(1500) // Simulate analysis time
        _analysisProgress.postValue(0.3f)
        
        // Simulate different analysis stages
        delay(1000)
        _analysisProgress.postValue(0.6f)
        
        delay(1000)
        _analysisProgress.postValue(0.9f)
        
        delay(500)
        
        // Get audio characteristics
        val audioLevel = getAudioLevel()
        val speechRate = estimateSpeechRate()
        val pitch = estimatePitch(audioLevel)
        
        // Determine mood based on characteristics
        val moodType = determineMood(audioLevel, speechRate, pitch)
        
        val moodRecord = MoodRecord(
            moodType = moodType,
            speechRate = speechRate,
            pitch = pitch,
            volume = audioLevel,
            confidence = 0.8f + (Math.random() * 0.2f).toFloat()
        )
        
        _analysisProgress.postValue(1.0f)
        _analysisResult.postValue(moodRecord)
    }
    
    fun estimateSpeechRate(): Float {
        // Simple estimation based on audio level fluctuations
        val level = getAudioLevel()
        return when {
            level < 0.1f -> 80f  // Slow, calm
            level < 0.2f -> 100f // Normal
            level < 0.4f -> 130f // Fast, excited
            else -> 160f         // Very fast, anxious
        }
    }
    
    fun estimatePitch(audioLevel: Float): Float {
        // Pitch estimation based on audio characteristics
        return when {
            audioLevel < 0.15f -> 200f // Lower pitch, calm
            audioLevel < 0.3f -> 250f  // Normal
            else -> 300f               // Higher pitch, excited/anxious
        }
    }
    
    fun determineMood(volume: Float, speechRate: Float, pitch: Float): MoodType {
        // Simple mood determination logic based on audio characteristics
        // In production, this would use ML models
        
        val score = (volume * 300 + speechRate + pitch / 2).toFloat()
        
        return when {
            score < 120f -> MoodType.CALM      // Slow, quiet
            score < 200f -> MoodType.NEUTRAL   // Normal
            score < 280f -> MoodType.EXCITED   // Fast, energetic
            else -> MoodType.ANXIOUS           // Very fast, high pitch
        }
    }
    
    fun cancelAnalysis() {
        analysisJob?.cancel()
        analysisJob = null
    }
    
    fun release() {
        stopRecording()
        mainScope.coroutineContext.cancelChildren()
        ioScope.coroutineContext.cancelChildren()
    }
}
