package com.zx_tole.pocketpsychologist.ui.breathing

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BrightnessHigh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.zx_tole.pocketpsychologist.R
import com.zx_tole.pocketpsychologist.ui.theme.MoodCalm
import com.zx_tole.pocketpsychologist.ui.theme.MoodExcited
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BreathingExerciseScreen(onClose: () -> Unit) {
    val context = LocalContext.current
    var breathingPhase by remember { mutableStateOf(BreathingPhase.INHALE) }
    var phaseTime by remember { mutableStateOf(0) }
    var isBreathing by remember { mutableStateOf(false) }
    
    val maxPhaseTime = 4000 // 4 seconds per phase
    
    // Breathing rhythm pattern
    val rhythmPattern = longArrayOf(0, 1000, 100, 1000, 100, 1000, 100, 1000, 100)
    
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    
    val breathColor = when (breathingPhase) {
        BreathingPhase.INHALE -> MoodCalm
        BreathingPhase.HOLD -> MoodExcited
        BreathingPhase.EXHALE -> MoodCalm
        BreathingPhase.REST -> MoodCalm
    }
    
    val breathText = when (breathingPhase) {
        BreathingPhase.INHALE -> R.string.inhale
        BreathingPhase.HOLD -> R.string.hold
        BreathingPhase.EXHALE -> R.string.exhale
        BreathingPhase.REST -> R.string.hold
    }
    
    val breathDescription = when (breathingPhase) {
        BreathingPhase.INHALE -> "Вдох"
        BreathingPhase.HOLD -> "Задержка"
        BreathingPhase.EXHALE -> "Выдох"
        BreathingPhase.REST -> "Задержка"
    }
    
    // Breathing animation loop
    LaunchedEffect(isBreathing) {
        while (isBreathing) {
            breathingPhase = BreathingPhase.INHALE
            phaseTime = 0
            delay(1000)
            
            for (i in 0..1) {
                breathingPhase = BreathingPhase.HOLD
                phaseTime = 0
                delay(1000)
                
                breathingPhase = BreathingPhase.EXHALE
                phaseTime = 0
                delay(1000)
                
                breathingPhase = BreathingPhase.REST
                phaseTime = 0
                delay(1000)
            }
        }
    }
    
    // Vibration effect
    LaunchedEffect(breathingPhase) {
        if (isBreathing && breathingPhase == BreathingPhase.INHALE) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                vibrator.vibrate(100)
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(stringResource(R.string.breathing_exercise)) },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Close")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        if (!isBreathing) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.BrightnessHigh,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MoodCalm
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.take_a_deep_breath),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.breathing_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { isBreathing = true },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(stringResource(R.string.start_breathing))
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(breathColor.copy(alpha = 0.8f), breathColor.copy(alpha = 0.2f))
                            )
                        )
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(breathColor.copy(alpha = 0.6f), breathColor.copy(alpha = 0.1f))
                                )
                            )
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(breathText),
                                style = MaterialTheme.typography.displayLarge,
                                color = breathColor,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = breathDescription,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(
                        Triple(R.string.inhale, MoodCalm, BreathingPhase.INHALE),
                        Triple(R.string.hold, MoodExcited, BreathingPhase.HOLD),
                        Triple(R.string.exhale, MoodCalm, BreathingPhase.EXHALE)
                    ).forEach { (textId, color, phase) ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = if (breathingPhase == phase) color else Color.Gray,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                if (breathingPhase == phase) {
                                    Box(
                                        modifier = Modifier
                                            .size(12.dp)
                                            .background(color, CircleShape)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = stringResource(textId),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                TextButton(
                    onClick = { isBreathing = false }
                ) {
                    Text(stringResource(R.string.finish_breathing))
                }
            }
        }
    }
}

enum class BreathingPhase {
    INHALE,
    HOLD,
    EXHALE,
    REST
}
