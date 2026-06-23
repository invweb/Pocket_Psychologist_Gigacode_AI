package com.zx_tole.pocketpsychologist.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.zx_tole.pocketpsychologist.R
import com.zx_tole.pocketpsychologist.data.model.MoodType
import com.zx_tole.pocketpsychologist.data.repository.MoodRepository
import com.zx_tole.pocketpsychologist.data.repository.MoodRepositoryImpl
import com.zx_tole.pocketpsychologist.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

class MoodWidget : AppWidgetProvider() {
    
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        appWidgetIds.forEach { appId ->
            updateAppWidget(context, appWidgetManager, appId)
        }
    }
    
    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }
    
    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.mood_widget_layout)
        
        // Get last mood
        val repository = MoodRepositoryImpl(context)
        
        // Use coroutine to fetch last mood
        CoroutineScope(Dispatchers.IO).launch {
            val lastMood = repository.getLastMood()
            val moodType = lastMood?.moodType ?: MoodType.NEUTRAL
            
            val moodText = when (moodType) {
                MoodType.CALM -> context.getString(R.string.mood_calm)
                MoodType.EXCITED -> context.getString(R.string.mood_excited)
                MoodType.ANXIOUS -> context.getString(R.string.mood_anxious)
                MoodType.SAD -> context.getString(R.string.mood_sad)
                else -> context.getString(R.string.mood_neutral)
            }
            
            // Update UI on main thread
            CoroutineScope(Dispatchers.Main).launch {
                views.setTextViewText(R.id.widget_mood_text, moodText)
                
                val timestamp = lastMood?.timestamp ?: System.currentTimeMillis()
                val timeAgo = getTimeAgo(timestamp)
                views.setTextViewText(R.id.widget_time, timeAgo)
                
                // Set click to open app
                val intent = Intent(context, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                val pendingIntent = PendingIntent.getActivity(
                    context, 0, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                views.setOnClickPendingIntent(R.id.widget_label, pendingIntent)
                
                // Update widget
                appWidgetManager.updateAppWidget(appWidgetId, views)
            }
        }
    }
    
    private fun getTimeAgo(timestamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timestamp
        
        return when {
            diff < 60000 -> "Только что"
            diff < 3600000 -> "${diff / 60000} мин. назад"
            diff < 86400000 -> "${diff / 3600000} ч. назад"
            else -> "${diff / 86400000} дн. назад"
        }
    }
}
