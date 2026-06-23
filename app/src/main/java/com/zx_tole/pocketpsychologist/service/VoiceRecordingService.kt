package com.zx_tole.pocketpsychologist.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.zx_tole.pocketpsychologist.R
import com.zx_tole.pocketpsychologist.ui.MainActivity

class VoiceRecordingService : Service() {
    
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_START_RECORDING) {
            startRecording()
        } else if (intent?.action == ACTION_STOP_RECORDING) {
            stopRecording()
        }
        return START_NOT_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startRecording() {
        if (isRecording) return
        
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128000)
            setAudioSamplingRate(44100)
            setOutputFile("/sdcard/record.mp4")
            
            try {
                prepare()
                start()
                isRecording = true
                startForeground(1, createNotification())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    
    private fun stopRecording() {
        mediaRecorder?.apply {
            try {
                stop()
                reset()
                release()
                isRecording = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        mediaRecorder = null
        stopForeground(true)
        stopSelf()
    }
    
    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, "voice_channel")
            .setContentTitle(getString(R.string.notification_recording_title))
            .setContentText(getString(R.string.notification_recording_text))
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(
                android.app.PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, MainActivity::class.java),
                    android.app.PendingIntent.FLAG_UPDATE_CURRENT or android.app.PendingIntent.FLAG_IMMUTABLE
                )
            )
            .build()
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "voice_channel",
            getString(R.string.notification_channel_name),
            android.app.NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = getString(R.string.notification_channel_description)
        }
        
        val notificationManager = getSystemService(android.app.NotificationManager::class.java)
        notificationManager.createNotificationChannel(channel)
    }
    
    companion object {
        const val ACTION_START_RECORDING = "com.zx_tole.pocketpsychologist.START_RECORDING"
        const val ACTION_STOP_RECORDING = "com.zx_tole.pocketpsychologist.STOP_RECORDING"
    }
}
