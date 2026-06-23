package com.zx_tole.pocketpsychologist.ui.breathing

import android.os.Bundle
import androidx.activity.ComponentActivity

class BreathingExerciseActivity : ComponentActivity() {
    companion object {
        const val START_FROM_HOME = "start_from_home"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // The actual UI is handled by BreathingExerciseScreen composable
    }
}
